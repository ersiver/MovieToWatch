package com.breiter.movietowatchapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.breiter.movietowatchapp.data.database.ILocalDataSource
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.network.MovieService
import com.breiter.movietowatchapp.data.util.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(
    private val movieService: MovieService,
    private val dataSource: ILocalDataSource
) : IMovieRepository {

    override val _searchedMovies = MutableLiveData<List<Movie>>()
    override val searchedMovies: LiveData<List<Movie>>
        get() = _searchedMovies

    override val _isSavedMovie = MutableLiveData<Boolean>()
    override val isSavedMovie: LiveData<Boolean>
        get() = _isSavedMovie

    override suspend fun insert(movie: Movie) {
        dataSource.insert(movie)
        _isSavedMovie.postValue(true)
    }

    override suspend fun delete(movie: Movie) {
        dataSource.delete(movie)
        _isSavedMovie.postValue(false)
    }

    override suspend fun setSaved(movie: Movie) {
        val isSaved = dataSource.isSaved(movie)
        _isSavedMovie.postValue(isSaved)
    }

    override fun getSavedMovies(): LiveData<List<Movie>> {
        return dataSource.getSavedMovies()
    }

    override fun getGenresNames(ids: List<Int>): LiveData<List<String>> {
        return dataSource.getGenresNames(ids)
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
            dataSource.insertAll(genresDeferred)
        }
    }
}