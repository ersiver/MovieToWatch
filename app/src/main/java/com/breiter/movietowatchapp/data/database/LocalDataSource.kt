package com.breiter.movietowatchapp.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.network.GenreResponseDTO
import com.breiter.movietowatchapp.data.util.asDatabaseModel
import com.breiter.movietowatchapp.data.util.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Concrete implementation of a data source as a db.

 * Coroutine suspend functions used to run in a background thread.
 *
 * Queries that return instances of LiveData automatically run
 * the query asynchronously on a background thread, when needed.
 *
 * Objects mapped to domain and or database objects accordingly.
*/

class LocalDataSource(private val movieDao: MovieDao, private val genreDao: GenreDao) :
    ILocalDataSource {

    override suspend fun insert(movie: Movie) {
        withContext(Dispatchers.IO) {
            movieDao.insert(movie.asDatabaseModel())
        }
    }

    override suspend fun delete(movie: Movie) {
        withContext(Dispatchers.IO) {
            movieDao.delete(movie.asDatabaseModel())
        }
    }

    override suspend fun isSaved(movie: Movie): Boolean {
        return withContext(Dispatchers.IO) {
            movieDao.moviesCount(movie.id) != 0
        }
    }

    override suspend fun insertAll(genres: GenreResponseDTO) {
       withContext(Dispatchers.IO){
           Timber.i("GENRE ID RESPONSE: ${genres.asDatabaseModel().size}")
           genreDao.insertAll(genres.asDatabaseModel())
       }
    }

    override fun getSavedMovies(): LiveData<List<Movie>> {
        return movieDao.getSavedMovies().map { it.asDomainModel() }
    }

    override fun getGenresNames(ids: List<Int>): LiveData<List<String>> {
        return genreDao.getGenres(ids)
    }
}