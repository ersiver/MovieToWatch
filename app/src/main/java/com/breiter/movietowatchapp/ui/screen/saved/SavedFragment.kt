package com.breiter.movietowatchapp.ui.screen.saved

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.databinding.SavedFragmentBinding
import com.breiter.movietowatchapp.ui.screen.saved.SavedMovieSwipeCallback.SwipeListener
import com.breiter.movietowatchapp.ui.util.getViewModelFactory

class SavedFragment : Fragment() {
    private val binding: SavedFragmentBinding by lazy {
        SavedFragmentBinding.inflate(layoutInflater)
    }
    private  val savedViewModel: SavedViewModel by viewModels{
        getViewModelFactory()
    }
    private lateinit var savedAdapter: SavedMovieAdapter
    private lateinit var onSwipeListener: SwipeListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        savedAdapter = SavedMovieAdapter(object : SavedMovieAdapter.OnClickListener{
            override fun onClick(movie: Movie) {
                savedViewModel.displayMovieDetails(movie)
            }
        })

        onSwipeListener = SwipeListener {
            savedViewModel.deleteMovie(savedAdapter.getMovieAt(it))
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = savedViewModel
            savedMoviesList.adapter = savedAdapter
            swipeListener = onSwipeListener
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideSoftInput()
        setupNavigationToDetail()
        setupNavigationToSearch()
    }

    private fun setupNavigationToSearch() {
        savedViewModel.navigateToSearch.observe(viewLifecycleOwner) {
            if (it != null) {
                this.findNavController()
                    .navigate(SavedFragmentDirections.actionSavedFragmentToSearchFragment())
                savedViewModel.navigateToSearchComplete()
            }
        }
    }

    private fun setupNavigationToDetail() {
        savedViewModel.navigateToSelectedMovie.observe(viewLifecycleOwner) {
            if (it != null) {
                this.findNavController()
                    .navigate(SavedFragmentDirections.actionSavedFragmentToDetailFragment(it))
                // Reset to prevent multiple navigation
                savedViewModel.navigateToDetailsCompleted()
            }
        }
    }

    private fun hideSoftInput() {
        val inputMethodManager =
            requireNotNull(activity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}