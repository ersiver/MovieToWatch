package com.breiter.movietowatchapp.ui.screen.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.breiter.movietowatchapp.databinding.DetailFragmentBinding
import com.breiter.movietowatchapp.ui.util.getViewModelFactory

class DetailFragment : Fragment() {
    private val detailViewModel by viewModels<DetailViewModel>{
        getViewModelFactory()
    }

    private val movie by lazy{
        DetailFragmentArgs.fromBundle(requireArguments()).selectedMovie
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DetailFragmentBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = detailViewModel
        }
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideSoftInput()

        detailViewModel.start(movie)
        setupNavigationToSaved()
        setupNavigationToSearch()
    }

    private fun setupNavigationToSearch() {
        detailViewModel.navigateToSearch.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(DetailFragmentDirections.actionDetailFragmentToSearchFragment())
                detailViewModel.navigateToSearchComplete()
            }
        })
    }

    private fun setupNavigationToSaved() {
        detailViewModel.navigateToSaved.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(DetailFragmentDirections.actionDetailFragmentToSavedFragment())
                detailViewModel.navigateToSavedCompleted()
            }
        })
    }

    private fun hideSoftInput() {
        val inputMethodManager =
            requireNotNull(activity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}