package com.breiter.movietowatchapp.ui.screen.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.repository.IMovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SavedViewModel(private val repository: IMovieRepository) : ViewModel() {
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
     * Called when app launches to display saved movies.
     * Creates _savedMovies MediatorLiveData, which observes repository
     * LiveData and will update list of saved movies onChanged events.
     */
    fun getSavedMovies() {
        _savedMovies.addSource(repository.getSavedMovies()) {
            _savedMovies.value = it
        }
    }

    /**
     * Executes once a movie is selected. Triggers navigation to DetailFragment.
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
     * Executes onSwipe.
     */
    fun deleteMovie(movie: Movie) {
        coroutineScope.launch {
            repository.delete(movie)
        }
    }

    /**
     * Executes once search_image icon is clicked and triggers navigation to the SearchFragment.
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
