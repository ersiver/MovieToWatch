package com.breiter.movietowatchapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
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

    suspend fun isSaved(movie: Movie) {
        withContext(Dispatchers.IO) {
            val isSaved = database.movieDao.moviesCount(movie.id) != 0
            _isSavedMovie.postValue(isSaved)
        }
    }

    suspend fun insert(movie: Movie) {
        withContext(Dispatchers.IO) {
            database.movieDao.insert(movie.asDatabaseModel())
        }
        _isSavedMovie.value = true
    }

    suspend fun delete(movie: Movie) {
        withContext(Dispatchers.IO) {
            database.movieDao.delete(movie.asDatabaseModel())
        }
        _isSavedMovie.value = false
    }

    suspend fun getSavedMovies(): LiveData<List<Movie>> {
        return withContext(Dispatchers.IO) {
            val movies: LiveData<List<Movie>> =
                database.movieDao.getSavedMovies().map { it.asDomainModel() }

            movies
        }
    }

    suspend fun getGenresNames(ids: List<Int>): LiveData<List<String>> {
        return withContext(Dispatchers.IO) {
            val genreNames: LiveData<List<String>> =
                database.genreDao.getGenres(ids)

            genreNames
        }
    }

    suspend fun getGenresFromNetwork() {
        val genresDeferred = retrofitClient.getGenresAsync().await()
        database.genreDao.insertAll(genresDeferred.asDatabaseModel())
    }

    suspend fun getMovies(title: String) {
        return withContext(Dispatchers.IO) {
            val moviesDeferred = retrofitClient.getMoviesAsync(title).await()
            _searchedMovies.postValue(moviesDeferred.asDomainModel())
        }
    }
}