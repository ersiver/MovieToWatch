package com.breiter.movietowatchapp.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.breiter.movietowatchapp.data.repository.IMovieRepository
import com.breiter.movietowatchapp.data.repository.MovieRepository

class SearchViewModelFactory(private val repository: IMovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(SearchViewModel::class.java))
            return SearchViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}