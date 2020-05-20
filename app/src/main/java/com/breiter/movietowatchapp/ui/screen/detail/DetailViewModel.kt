package com.breiter.movietowatchapp.ui.screen.detail

import android.app.Application
import androidx.lifecycle.*
import com.breiter.movietowatchapp.data.database.MovieDatabase
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.network.RetrofitClient
import com.breiter.movietowatchapp.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailViewModel(val app: Application, val movie: Movie) : AndroidViewModel(app) {
    private val repository = MovieRepository(MovieDatabase.getInstance(app), RetrofitClient())
    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie>
        get() = _selectedMovie

    private val _isSaved = repository.isSavedMovie
    val isSaved: LiveData<Boolean>
        get() = _isSaved

    private val _navigateToSearch = MutableLiveData<Boolean>()
    val navigateToSearch: LiveData<Boolean>
        get() = _navigateToSearch

    private val _navigateToSaved = MutableLiveData<Boolean>()
    val navigateToSaved: LiveData<Boolean>
        get() = _navigateToSaved

    private val _genreNames = MediatorLiveData<List<String>>()
    val genreNames: LiveData<List<String>>
        get() = _genreNames

    init {
        _selectedMovie.value = movie
        getGenresNames()
        checkIfSaved()
    }

    /**
     * Get genres as list of String via repository.
     */
    private fun getGenresNames() {
        coroutineScope.launch {
            _genreNames.addSource(repository.getGenresNames(movie.genreIds!!)) {
                _genreNames.value = it
            }
        }
    }

    /**
     * Triggers displaying correct icon to add or remove a movie
     */
    private fun checkIfSaved() {
        coroutineScope.launch {
            repository.isSaved(movie)
        }
    }

    fun onAddClick() {
        _isSaved.value?.let { isSaved ->
            if (isSaved)
                removeFromSaved()
            else
                saveMovie()
        }
    }

    private fun removeFromSaved() {
        coroutineScope.launch {
            repository.delete(movie)

        }
    }

    private fun saveMovie() {
        coroutineScope.launch {
            repository.insert(movie)
        }
    }

    /**
     * Executes once SEARCH icon is clicked and navigate to the search fragment
     */
    fun onSearchClick() {
        _navigateToSearch.value = true
    }

    /**
     * Resets value of _navigateToSearchFragment once the navigation has taken place.
     */
    fun navigateToSearchComplete() {
        _navigateToSearch.value = null
    }

    /**
     * Executes once SAVED icon is clicked and triggers navigation to saved fragment.
     */
    fun onSavedClicked() {
        _navigateToSaved.value = true
    }

    /**
     * Resets value of _navigateToSaved once the navigation has taken place.
     */
    fun navigateToSavedCompleted() {
        _navigateToSaved.value = null
    }

    /**
     * Cancel coroutine, when view model is finished
     */
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
