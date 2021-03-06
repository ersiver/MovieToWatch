package com.breiter.movietowatchapp.ui.screen.detail

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.repository.IMovieRepository
import kotlinx.coroutines.*

class DetailViewModel(private val repository: IMovieRepository) : ViewModel() {
    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie>
        get() = _selectedMovie

    lateinit var genreAsString: LiveData<List<String>>

    val isSaved: LiveData<Boolean> = repository.isSavedMovie

    private val _navigateToSearch = MutableLiveData<Boolean>()
    val navigateToSearch: LiveData<Boolean>
        get() = _navigateToSearch

    private val _navigateToSaved = MutableLiveData<Boolean>()
    val navigateToSaved: LiveData<Boolean>
        get() = _navigateToSaved

    fun start(movie: Movie) {
        _selectedMovie.value = movie
        initGenres()
        initSavedStatus(movie)

    }

    /**
     * Triggers loading of a correct icon
     * (save or delete) to the favourite_image.
     */
    private fun initSavedStatus(movie: Movie) {
        coroutineScope.launch {
            repository.setSaved(movie)
        }
    }

    private fun initGenres() {
        genreAsString = selectedMovie.switchMap { movie ->
            repository.getGenresNames(movie.genreIds)
        }
    }

    /**
     * Executes when the favourite_image is clicked.
     * Will save or delete the movie respectively.
     */
    fun onAddRemoveClick() {
        isSaved.value?.let {
            if(it) removeFromSaved() else saveMovie()
        }
    }

    @VisibleForTesting
    internal fun removeFromSaved() {
        coroutineScope.launch {
            selectedMovie.value?.let {
                repository.delete(it)
            }
        }
    }

    @VisibleForTesting
    internal fun saveMovie() {
        coroutineScope.launch {
            selectedMovie.value?.let {
                repository.insert(it)
            }
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