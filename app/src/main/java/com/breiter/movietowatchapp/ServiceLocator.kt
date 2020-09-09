package com.breiter.movietowatchapp

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.breiter.movietowatchapp.data.database.ILocalDataSource
import com.breiter.movietowatchapp.data.database.LocalDataSource
import com.breiter.movietowatchapp.data.database.MovieDatabase
import com.breiter.movietowatchapp.data.network.MovieService
import com.breiter.movietowatchapp.data.repository.IMovieRepository
import com.breiter.movietowatchapp.data.repository.MovieRepository

/**
 * A Service Locator for the [MovieRepository].
 * This is the prod version, with a the "real" [LocalDataSource].
 */

object ServiceLocator {
    private val lock = Any()
    private var database: MovieDatabase? = null

    @Volatile
    var repository: IMovieRepository? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): IMovieRepository {
        synchronized(this) {
            return repository ?: createRepository(context)
        }
    }

    private fun createRepository(context: Context): IMovieRepository {
        val newRepo = MovieRepository(MovieService.create(), createLocalDataSource(context))
        repository = newRepo
        return newRepo
    }

    private fun createLocalDataSource(context: Context): ILocalDataSource {
        val database = database ?: createDatabase(context)
        return LocalDataSource(database.movieDao, database.genreDao)
    }

    private fun createDatabase(context: Context): MovieDatabase {
        val instance = Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "movie_db"
        ).build()
        database = instance
        return instance
    }


    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
// Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            repository = null
        }
    }
}