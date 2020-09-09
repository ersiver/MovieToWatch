package com.breiter.movietowatchapp.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.breiter.movietowatchapp.data.database.DatabaseMovie
import com.breiter.movietowatchapp.data.database.MovieDao
import com.breiter.movietowatchapp.data.database.MovieDatabase
import com.breiter.movietowatchapp.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection.empty
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * JUnit instrumental test for Room database.
 */
@SmallTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MovieDatabaseTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var db: MovieDatabase
    private lateinit var movieDao: MovieDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java).build()
        movieDao = db.movieDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndRead() = runBlockingTest{
        val movie =
            DatabaseMovie(id = 22)
        movieDao.insert(movie)

        val saved = movieDao.getSavedMovies().getOrAwaitValue()
        assertThat(saved[0].id, equalTo(movie.id))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDelete() = runBlockingTest {
        val movie =
            DatabaseMovie(id = 22)
        movieDao.insert(movie)
        movieDao.delete(movie)
        val saved = movieDao.getSavedMovies().getOrAwaitValue()
        assertThat(saved, empty())
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetById() = runBlockingTest{
        for(i in 1..10){
            val movie =
                DatabaseMovie(id = i)
            movieDao.insert(movie)
        }
        val moviesCount = movieDao.moviesCount(10)
        assertThat(moviesCount, `is`(1))
    }
}