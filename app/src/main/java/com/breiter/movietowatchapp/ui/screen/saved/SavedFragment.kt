package com.breiter.movietowatchapp.ui.screen.saved

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.breiter.movietowatchapp.databinding.SavedFragmentBinding
import com.breiter.movietowatchapp.ui.screen.saved.SavedMovieSwipeCallback.SwipeListener

class SavedFragment : Fragment() {
    private val binding: SavedFragmentBinding by lazy {
        SavedFragmentBinding.inflate(layoutInflater)
    }

    private val app: Application by lazy {
        requireNotNull(activity).application
    }

    private val savedViewModel: SavedViewModel by lazy {
        ViewModelProvider(
                this,
                SavedViewModelFactory(app)
        ).get(SavedViewModel::class.java)
    }

    private lateinit var savedAdapter: SavedMovieAdapter

    private lateinit var onSwipeListener: SwipeListener

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        savedAdapter = SavedMovieAdapter(SavedMovieAdapter.OnClickListener {
            savedViewModel.displayMovieDetails(it)
        })

        onSwipeListener = SwipeListener {
            savedViewModel.deleteMovie(savedAdapter.getMovieAt(it))
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = savedViewModel
            savedMoviesRecyclerview.adapter = savedAdapter
            swipeListener = onSwipeListener
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideSoftInput()

        // Observe the navigateToSelectedMovie LiveData and Navigate when it isn't null.
        savedViewModel.navigateToSelectedMovie.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                        .navigate(SavedFragmentDirections.actionSavedFragmentToDetailFragment(it))

                // Reset to prevent multiple navigation
                savedViewModel.navigateToDetailsCompleted()
            }
        })

        // Observe the navigateToSearchFragment LiveData and Navigate when it isn't null.
        savedViewModel.navigateToSearch.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                        .navigate(SavedFragmentDirections.actionSavedFragmentToSearchFragment())
                savedViewModel.navigateToSearchComplete()
            }
        })
    }

    private fun hideSoftInput() {
        val inputMethodManager =
                requireNotNull(activity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}



