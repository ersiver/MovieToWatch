package com.breiter.movietowatchapp.ui.screen.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.breiter.movietowatchapp.databinding.SearchFragmentBinding

class SearchFragment : Fragment() {
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = SearchFragmentBinding.inflate(inflater)

        val app = requireNotNull(activity).application

        searchViewModel =
            ViewModelProvider(this, SearchViewModelFactory(app)).get(SearchViewModel::class.java)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = searchViewModel
            searchedMovieList.adapter = SearchMovieAdapter(SearchMovieAdapter.OnClickListener {
                searchViewModel.displayMovieDetails(it)
            })
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Observe the navigateToSelectedMovie LiveData and Navigate when it isn't null.
        searchViewModel.navigateToSelectedMovie.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(SearchFragmentDirections.actionSearchFragmentToDetailFragment(it))

                // Reset to prevent multiple navigation
                searchViewModel.navigateToDetailsCompleted()
            }
        })

        // Observe the navigateToSaved LiveData and Navigate when it isn't null.
        searchViewModel.navigateToSaved.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(SearchFragmentDirections.actionSearchFragmentToSavedFragment())

                // Reset to prevent multiple navigation
                searchViewModel.navigateToSavedCompleted()
            }
        })
    }
}
