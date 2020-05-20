package com.breiter.movietowatchapp.ui.screen.saved

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

class SavedViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = MovieRepository(MovieDatabase.getInstance(app), RetrofitClient())

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _savedMovies = MediatorLiveData<List<Movie>>()
    val savedMovies: LiveData<List<Movie>>
        get() = _savedMovies

    private val _navigateToSelectedMovie = MutableLiveData<Movie>()
    val navigateToSelectedMovie: LiveData<Movie>
        get() = _navigateToSelectedMovie

    private val _navigateToSearch = MutableLiveData<Boolean>()
    val navigateToSearch: LiveData<Boolean>
        get() = _navigateToSearch

    init {
        getSavedMovies()
    }

    /**
     * Called when app launches to display saved data.
     * Gets _savedMovies from database via repository.
     * Observes changes and update _savedMovies.
     */
    private fun getSavedMovies() {
        coroutineScope.launch {
            _savedMovies.addSource(repository.getSavedMovies()) {
                _savedMovies.value = it
            }
        }
    }

    /**
     * Executes once a movie item is clicked.
     * Triggers navigation to detail fragment.
     */
    fun displayMovieDetails(movie: Movie) {
        _navigateToSelectedMovie.value = movie
    }

    /**
     * Resets value of _navigateToSelectedMovie once the navigation has taken place.
     */
    fun navigateToDetailsCompleted() {
        _navigateToSelectedMovie.value = null
    }

    /**
     * Sets the value of _deleteMovie to the movie that
     * was swiped and deletes it from a database.
     */
    fun deleteMovie(movie: Movie) {
        coroutineScope.launch {
            repository.delete(movie)
            // repository.getSavedMovies()
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
     * Cancel coroutine, when view model is finished
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}


