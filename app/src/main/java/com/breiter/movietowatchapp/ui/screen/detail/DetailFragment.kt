package com.breiter.movietowatchapp.ui.screen.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.breiter.movietowatchapp.data.database.MovieDatabase
import com.breiter.movietowatchapp.data.network.getMovieApiService
import com.breiter.movietowatchapp.data.repository.MovieRepository
import com.breiter.movietowatchapp.databinding.DetailFragmentBinding

class DetailFragment : Fragment() {
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DetailFragmentBinding.inflate(inflater)

        detailViewModel = obtainViewModel()

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = detailViewModel
        }

        return binding.root
    }

    // Get DetailViewModel by passing a repository and a movie to the factory
    private fun obtainViewModel() : DetailViewModel {
        val app = requireNotNull(activity).application
        val repository = MovieRepository(getMovieApiService(), MovieDatabase.getInstance(app))
        val movie = DetailFragmentArgs.fromBundle(requireArguments()).selectedMovie

        return ViewModelProvider(
            this,
            DetailViewModelFactory(repository, movie)
        ).get(
            DetailViewModel::class.java
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideSoftInput()

        detailViewModel.navigateToSaved.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(DetailFragmentDirections.actionDetailFragmentToSavedFragment())

                detailViewModel.navigateToSavedCompleted()
            }
        })

        detailViewModel.navigateToSearch.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(DetailFragmentDirections.actionDetailFragmentToSearchFragment())

                detailViewModel.navigateToSearchComplete()
            }
        })
    }

    private fun hideSoftInput() {
        val inputMethodManager =
            requireNotNull(activity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}