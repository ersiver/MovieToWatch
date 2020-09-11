package com.breiter.movietowatchapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.breiter.movietowatchapp.util.MainCoroutineRule
import com.breiter.movietowatchapp.util.TestUtil
import com.breiter.movietowatchapp.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieRepositoryTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: MovieRepository
    private lateinit var fakeService: FakeService
    private lateinit var fakeDataSource: FakeDataSource

    @Before
    fun setUp() {
        fakeService = FakeService()
        fakeDataSource = FakeDataSource()
        repository = MovieRepository(fakeService, fakeDataSource)
    }


    @Test
    fun insertMovie_saveToDataSource() = runBlockingTest {
        val newMovie = TestUtil.createMovie(0, "TITLE")
        assertThat(fakeDataSource.movies, not(hasItem(newMovie)))
        repository.insert(newMovie)
        assertThat(fakeDataSource.movies, hasItem(newMovie))
    }

    @Test
    fun insertMovie_isSaveTrue() = runBlockingTest {
        val newMovie = TestUtil.createMovie(0, "TITLE")
        repository.setSaved(newMovie)
        var isSavedValue = repository.isSavedMovie.getOrAwaitValue()
        assertThat(isSavedValue, `is`(false))

        repository.insert(newMovie)
        isSavedValue = repository.isSavedMovie.getOrAwaitValue()
        assertThat(isSavedValue, `is`(true))
    }

    @Test
    fun deleteMovie_deleteFromDataSource() = runBlockingTest {
        val savedMovie = fakeDataSource.movies[0]
        assertThat(fakeDataSource.movies, hasItem(savedMovie))
        repository.delete(savedMovie)
        assertThat(fakeDataSource.movies, not(hasItem(savedMovie)))
    }

    @Test
    fun deleteMovie_isSaveFalse() = runBlockingTest {
        val savedMovie = fakeDataSource.movies[0]
        repository.setSaved(savedMovie)
        var isSavedValue = repository.isSavedMovie.getOrAwaitValue()
        assertThat(isSavedValue, `is`(true))

        repository.delete(savedMovie)
        isSavedValue = repository.isSavedMovie.getOrAwaitValue()
        assertThat(isSavedValue, `is`(false))
    }

    @Test
    fun getSavedMovies_savedMoviesValueMatchingDb() {
        val savedMovies = fakeDataSource.movies
        val result = repository.getSavedMovies().getOrAwaitValue()

        result.forEachIndexed { i, _ ->
            assertThat(result[i], `is`(savedMovies[i]))
        }
    }

    @Test
    fun getMoviesFromNetwork_updateMovieList() = runBlocking {
        repository.getMovies("TITLE1")
        val searchedMoviesValue = repository.searchedMovies.getOrAwaitValue()

        assertThat(searchedMoviesValue.size, `is`(2))

        for (movie in searchedMoviesValue) {
            assertThat(movie.title, `is`("TITLE1"))
        }
    }
}