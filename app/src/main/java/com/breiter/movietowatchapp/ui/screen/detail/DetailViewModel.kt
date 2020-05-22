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

    lateinit var genreAsString: LiveData<List<String>>

    val isSaved: LiveData<Boolean> = repository.isSavedMovie

    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie>
        get() = _selectedMovie

    private val _navigateToSearch = MutableLiveData<Boolean>()
    val navigateToSearch: LiveData<Boolean>
        get() = _navigateToSearch

    private val _navigateToSaved = MutableLiveData<Boolean>()
    val navigateToSaved: LiveData<Boolean>
        get() = _navigateToSaved

    init {
        _selectedMovie.value = movie
        getGenresNames()
        checkIfSaved()
    }

    /**
     * Creates a genreAsString LiveData, that depends on selectedMovie's genres' id.
     */
    private fun getGenresNames() {
        genreAsString = selectedMovie.switchMap { movie ->
            repository.getGenresNames(movie.genreIds!!)
        }
    }

    /**
     * Triggers loading of a correct icon (save or delete) to the favourite_image.
     */
    private fun checkIfSaved() {
        coroutineScope.launch {
            repository.isSaved(movie)
        }
    }

    /**
     * Executes when the favourite_image is clicked.
     * Will save or delete the movie respectively.
     */
    fun onAddClick() {
        isSaved.value?.let { isSaved ->
            when (isSaved) {
                true -> removeFromSaved()
                false -> saveMovie()
            }
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
     * Executes once search_image is clicked and triggers navigation to the SearchFragment.
     */
    fun onSearchClick() {
        _navigateToSearch.value = true
    }

    /**
     * Resets value of _navigateToSearchFragment LiveData once the navigation has taken place.
     */
    fun navigateToSearchComplete() {
        _navigateToSearch.value = null
    }

    /**
     * Executes once saved_movies is clicked and triggers navigation to SavedFragment.
     */
    fun onSavedClicked() {
        _navigateToSaved.value = true
    }

    /**
     * Resets value of _navigateToSaved LiveData once the navigation has taken place.
     */
    fun navigateToSavedCompleted() {
        _navigateToSaved.value = null
    }

    /**
     * Cancel coroutine, when viewModel is finished
     */
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
