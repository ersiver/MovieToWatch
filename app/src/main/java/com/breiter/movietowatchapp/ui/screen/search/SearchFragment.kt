package com.breiter.movietowatchapp.ui.screen.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.breiter.movietowatchapp.data.database.MovieDatabase
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.network.getMovieApiService
import com.breiter.movietowatchapp.data.repository.MovieRepository
import com.breiter.movietowatchapp.databinding.SearchFragmentBinding
import com.breiter.movietowatchapp.ui.screen.search.SearchMovieAdapter.OnClickListener

class SearchFragment : Fragment() {
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var binding: SearchFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = SearchFragmentBinding.inflate(inflater, container, false)

        searchViewModel = obtainViewModel()

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

    private fun obtainViewModel(): SearchViewModel {
        val app = requireNotNull(activity).application
        val repository = MovieRepository(
            getMovieApiService(), MovieDatabase.getInstance(app))

        return ViewModelProvider(
            this,
            SearchViewModelFactory(repository)
        ).get(SearchViewModel::class.java)
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
