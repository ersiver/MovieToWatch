package com.breiter.movietowatchapp.ui.screen.detail

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
import com.breiter.movietowatchapp.databinding.DetailFragmentBinding


class DetailFragment : Fragment() {
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DetailFragmentBinding.inflate(inflater)

        val app = requireNotNull(activity).application

        val movie = DetailFragmentArgs.fromBundle(requireArguments()).selectedMovie

        detailViewModel = ViewModelProvider(
            this,
            DetailViewModelFactory(app, movie)
        ).get(
            DetailViewModel::class.java
        )

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = detailViewModel
        }

        return binding.root
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

