package com.breiter.movietowatchapp.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.breiter.movietowatchapp.data.repository.IMovieRepository
import com.breiter.movietowatchapp.ui.screen.detail.DetailViewModel
import com.breiter.movietowatchapp.ui.screen.saved.SavedViewModel
import com.breiter.movietowatchapp.ui.screen.search.SearchViewModel

/**
 * Factory for all ViewModels
 */

class ViewModelFactory(private val repository: IMovieRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(SavedViewModel::class.java) -> SavedViewModel(repository)
                isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(repository)
                isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(repository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
                    as T
        }
}