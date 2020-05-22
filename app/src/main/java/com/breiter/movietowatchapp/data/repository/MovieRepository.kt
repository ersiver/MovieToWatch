package com.breiter.movietowatchapp.data.repository

import androidx.lifecycle.*
import com.breiter.movietowatchapp.data.database.MovieDatabase
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.network.RetrofitClient
import com.breiter.movietowatchapp.data.util.asDatabaseModel
import com.breiter.movietowatchapp.data.util.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MovieRepository(
    private val database: MovieDatabase,
    private val retrofitClient: RetrofitClient
) {

    private val _searchedMovies = MutableLiveData<List<Movie>>()
    val searchedMovies: LiveData<List<Movie>>
        get() = _searchedMovies

    private val _isSavedMovie = MutableLiveData<Boolean>()
    val isSavedMovie: LiveData<Boolean>
        get() = _isSavedMovie


    /**
     * Room operations.
     * Use a coroutine suspend function to run in a background thread.
     */
    suspend fun insert(movie: Movie) {
        withContext(Dispatchers.IO) {
            database.movieDao.insert(movie.asDatabaseModel())
            _isSavedMovie.postValue(true)
        }
    }

    suspend fun delete(movie: Movie) {
        withContext(Dispatchers.IO) {
            database.movieDao.delete(movie.asDatabaseModel())
            _isSavedMovie.postValue(false)
        }
    }

    suspend fun isSaved(movie: Movie) {
        withContext(Dispatchers.IO) {
            val isSaved = database.movieDao.moviesCount(movie.id) != 0
            _isSavedMovie.postValue(isSaved)
        }
    }

    /**
     * Queries that return instances of LiveData automatically run
     * the query asynchronously on a background thread, when needed.
     *
     */
    fun getSavedMovies(): LiveData<List<Movie>> {
        return database.movieDao.getSavedMovies().map { it.asDomainModel() }
    }

    fun getGenresNames(ids: List<Int>): LiveData<List<String>> {
        return database.genreDao.getGenres(ids)
    }

    /**
     * Network operations. Use a coroutine suspend functions
     *  to run in a background thread.
     */
    suspend fun getMovies(title: String) {
        return withContext(Dispatchers.IO) {
            val moviesDeferred = retrofitClient.getMoviesAsync(title).await()
            _searchedMovies.postValue(moviesDeferred.asDomainModel())
        }
    }

    suspend fun getGenresFromNetwork() {
        withContext(Dispatchers.IO) {
            val genresDeferred = retrofitClient.getGenresAsync().await()
            database.genreDao.insertAll(genresDeferred.asDatabaseModel())
        }
    }
}