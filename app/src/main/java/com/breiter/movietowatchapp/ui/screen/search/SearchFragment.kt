package com.breiter.movietowatchapp.ui.screen.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.databinding.SearchFragmentBinding
import com.breiter.movietowatchapp.ui.screen.search.SearchMovieAdapter.OnClickListener
import com.breiter.movietowatchapp.ui.util.getViewModelFactory

class SearchFragment : Fragment() {
    private val searchViewModel: SearchViewModel by viewModels {
        getViewModelFactory()
    }
    private lateinit var binding: SearchFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = searchViewModel

            val onClick: OnClickListener = object : OnClickListener{
                override fun onClick(movieItem: Movie) {
                    searchViewModel.displayMovieDetails(movieItem)
                }
            }

            searchedMovieList.adapter = SearchMovieAdapter(onClick)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupNavigationToDetails()
        setupNavigationToSaved()
    }

    private fun setupNavigationToSaved() {
        searchViewModel.navigateToSaved.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(SearchFragmentDirections.actionSearchFragmentToSavedFragment())
                searchViewModel.navigateToSavedCompleted()
            }
        })
    }

    private fun setupNavigationToDetails() {
        searchViewModel.navigateToSelectedMovie.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(SearchFragmentDirections.actionSearchFragmentToDetailFragment(it))
                searchViewModel.navigateToDetailsCompleted()
            }
        })
    }
}