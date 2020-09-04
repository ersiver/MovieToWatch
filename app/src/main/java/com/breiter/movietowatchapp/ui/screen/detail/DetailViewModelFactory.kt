package com.breiter.movietowatchapp.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.repository.MovieRepository

class DetailViewModelFactory(private val repository: MovieRepository, val movie: Movie) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(DetailViewModel::class.java))
            return DetailViewModel(repository, movie) as T
        throw IllegalArgumentException("Unknown viewModel class")
    }
}