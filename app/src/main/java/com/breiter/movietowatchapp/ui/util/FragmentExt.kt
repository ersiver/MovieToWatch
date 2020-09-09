package com.breiter.movietowatchapp.ui.util

import androidx.fragment.app.Fragment
import com.breiter.movietowatchapp.MovieToWatchApplication
import com.breiter.movietowatchapp.ui.screen.ViewModelFactory


//To avoid building a seperate factory for
// each view model use this extension function.

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository =
        (requireContext().applicationContext as MovieToWatchApplication).movieRepository
    return ViewModelFactory(repository)
}