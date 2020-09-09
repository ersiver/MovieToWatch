package com.breiter.movietowatchapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.breiter.movietowatchapp.data.database.MovieDatabase
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.network.MovieService
import com.breiter.movietowatchapp.data.util.asDatabaseModel
import com.breiter.movietowatchapp.data.util.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(
    private val movieService: MovieService,
    private val database: MovieDatabase
) : IMovieRepository {

    override val _searchedMovies = MutableLiveData<List<Movie>>()
    override val searchedMovies: LiveData<List<Movie>>
        get() = _searchedMovies

    override val _isSavedMovie = MutableLiveData<Boolean>()
    override val isSavedMovie: LiveData<Boolean>
        get() = _isSavedMovie

    /**
     * Room operations.
     * Use a coroutine suspend function to run in a background thread.
     */
    override suspend fun insert(movie: Movie) {
        withContext(Dispatchers.IO) {
            database.movieDao.insert(movie.asDatabaseModel())
            _isSavedMovie.postValue(true)
        }
    }

    override suspend fun delete(movie: Movie) {
        withContext(Dispatchers.IO) {
            database.movieDao.delete(movie.asDatabaseModel())
            _isSavedMovie.postValue(false)
        }
    }

    override suspend fun setSaved(movie: Movie) {
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
    override fun getSavedMovies(): LiveData<List<Movie>> {
        return database.movieDao.getSavedMovies().map { it.asDomainModel() }
    }

    override fun getGenresNames(ids: List<Int>): LiveData<List<String>> {
        return database.genreDao.getGenres(ids)
    }

    /**
     * Network operations. Use a coroutine suspend functions
     *  to run in a background thread.
     */
    override suspend fun getMovies(title: String) {
        return withContext(Dispatchers.IO) {
            val moviesDeferred =
                movieService.getMoviesAsync(title = title).await()

            _searchedMovies.postValue(moviesDeferred.asDomainModel())
        }
    }

    override suspend fun getGenresFromNetwork() {
        withContext(Dispatchers.IO) {
            val genresDeferred = movieService.getGenresAsync().await()
            database.genreDao.insertAll(genresDeferred.asDatabaseModel())
        }
    }
}
