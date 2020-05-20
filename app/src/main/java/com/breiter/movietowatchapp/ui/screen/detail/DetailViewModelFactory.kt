package com.breiter.movietowatchapp.ui.screen.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.breiter.movietowatchapp.data.domain.Movie
import java.lang.IllegalArgumentException

class DetailViewModelFactory(val app: Application, val movie: Movie) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(DetailViewModel::class.java))
            return DetailViewModel(app, movie) as T
        throw IllegalArgumentException("Uknwown viewModel class")
    }

}