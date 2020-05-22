package com.breiter.movietowatchapp.ui.screen.saved

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException


class SavedViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(SavedViewModel::class.java))
            return SavedViewModel(app) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}