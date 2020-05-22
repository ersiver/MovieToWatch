package com.breiter.movietowatchapp.ui.screen.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.breiter.movietowatchapp.data.database.MovieDatabase
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.network.RetrofitClient
import com.breiter.movietowatchapp.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = MovieRepository(MovieDatabase.getInstance(app), RetrofitClient())
    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private val _searchedMovies = repository.searchedMovies
    val searchedMovies: LiveData<List<Movie>>
        get() = _searchedMovies

    private val _navigateToSelectedMovie = MutableLiveData<Movie>()
    val navigateToSelectedMovie: LiveData<Movie>
        get() = _navigateToSelectedMovie

    private val _navigateToSaved = MutableLiveData<Boolean>()
    val navigateToSaved: LiveData<Boolean>
        get() = _navigateToSaved

    private val _status = MutableLiveData<NetworkStatus>()
    val status: LiveData<NetworkStatus>
        get() = _status

    private val query = MutableLiveData<String>()

    private val _clearQuery = MutableLiveData<Boolean>()
    val clearQuery: LiveData<Boolean>
        get() = _clearQuery


    /**
     * Listens any of the changes in the EditText and sets value of query LiveData to user's input.
     */
    fun onQueryTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        query.value = s?.trim().toString()
        startSearching()
    }

    /**
     * Invokes getMovieList function and sets value of _clearQuery to false,
     * if the value of user's input is not an empty string.
     */
    private fun startSearching() {
        query.value?.let {
            if (!it.isBlank()) {
                getMovieList(it)
                _clearQuery.value = false
            }
        }
    }

    /**
     * Gets _searchedMovies from network via repository.
     */
    private fun getMovieList(query: String) {
        coroutineScope.launch {
            try {
                repository.getMovies(query)
                _status.value = NetworkStatus.DONE
            } catch (error: Throwable) {
                Timber.i("Error message: $error")
                _status.value = NetworkStatus.ERROR
            }
        }
    }

    /**
     * Executes once cancel button is clicked and triggers clearing of the EditText via BindingAdapter.
     * Triggers clearing of a data list via BindingAdapter, so the recycler view is empty.
     */
    fun onClearClicked() {
        _clearQuery.value = true
    }

    /**
     * Executes once a movie is selected. Triggers navigation to DetailFragment.
     */
    fun displayMovieDetails(movie: Movie) {
        _navigateToSelectedMovie.value = movie
    }

    /**
     *  Resets value of _navigateToSelectedMovie once the navigation has taken place.
     */
    fun navigateToDetailsCompleted() {
        _navigateToSelectedMovie.value = null
    }

    /**
     * Executes once saved_movies is clicked and triggers navigation to SavedFragment.

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

enum class NetworkStatus { ERROR, DONE }