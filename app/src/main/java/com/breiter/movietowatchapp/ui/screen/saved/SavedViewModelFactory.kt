package com.breiter.movietowatchapp.ui.screen.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.breiter.movietowatchapp.data.repository.IMovieRepository
import com.breiter.movietowatchapp.data.repository.MovieRepository

class SavedViewModelFactory(private val repository: IMovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(SavedViewModel::class.java))
            return SavedViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}