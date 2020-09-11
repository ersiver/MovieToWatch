package com.breiter.movietowatchapp.ui.screen.saved

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.breiter.movietowatchapp.ServiceLocator
import com.breiter.movietowatchapp.util.FakeRepository
import com.breiter.movietowatchapp.util.MainCoroutineRule
import com.breiter.movietowatchapp.util.TestUtil
import com.breiter.movietowatchapp.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

/**
 * Local unit tests for the implementation of [SavedViewModel]
 */
@ExperimentalCoroutinesApi
class SavedViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: SavedViewModel
    private lateinit var repository: FakeRepository

    @Before
    fun setUp() = runBlockingTest {
        repository = FakeRepository()
        ServiceLocator.repository = repository

        // Each test is initialised with 3 saved movies.
        val movie1 = TestUtil.createMovie(1, "TITLE1")
        val movie2 = TestUtil.createMovie(2, "TITLE2")
        val movie3 = TestUtil.createMovie(3, "TITLE3")
        repository.add(movie1, movie2, movie3)

        viewModel = SavedViewModel(repository)
    }

    @After
    fun tearDown() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun start_displaySavedMovies() {
        val savedMoviesData = viewModel.savedMovies.getOrAwaitValue()
        assertThat(savedMoviesData.size, `is`(3))
        assertThat(savedMoviesData[0].title, `is`("TITLE1"))
        assertThat(savedMoviesData[0].id, `is`(1))
        assertThat(savedMoviesData[1].title, `is`("TITLE2"))
        assertThat(savedMoviesData[1].id, `is`(2))
        assertThat(savedMoviesData[2].title, `is`("TITLE3"))
        assertThat(savedMoviesData[2].id, `is`(3))
    }

    @Test
    fun deleteMovie_reducedMovieList() = runBlockingTest {
        //Make sure the initial list's size is 3.
        var savedMoviesData = viewModel.savedMovies.getOrAwaitValue()
        assertThat(savedMoviesData.size, `is`(3))

        //Add a new movie, and verify size is 4 now.
        val movie = TestUtil.createMovie(4, "TITLE4")
        repository.insert(movie)
        viewModel.getSavedMovies()
        savedMoviesData = viewModel.savedMovies.getOrAwaitValue()
        assertThat(savedMoviesData.size, `is`(4))

        //Delete a movie and verify size is reduced.
        viewModel.deleteMovie(movie)
        viewModel.getSavedMovies()
         savedMoviesData = viewModel.savedMovies.getOrAwaitValue()
        assertThat(savedMoviesData.size, `is`(3))
    }

    @Test
    fun onSearchClick_navigateToSearchTrue() {
        viewModel.onSearchClick()
        val navSearchEvent = viewModel.navigateToSearch.getOrAwaitValue()
        assertThat(navSearchEvent, `is`(true))
    }

    @Test
    fun navigateToSearchComplete_navigateToSearchNull() {
        viewModel.navigateToSearchComplete()
        val navSearchEvent = viewModel.navigateToSearch
        assertThat(navSearchEvent.getOrAwaitValue(), `is`(nullValue()))
    }

    @Test
    fun displayMovieDetails_navigateToMovie() {
        val movie = TestUtil.createMovie(1, "TITLE1")
        viewModel.displayMovieDetails(movie)
        val navDetailEvent = viewModel.navigateToSelectedMovie.getOrAwaitValue()
        assertThat(navDetailEvent, `is`(movie))
    }

    @Test
    fun displayMovieDetails_navigateToMovieNull() {
        viewModel.navigateToDetailsCompleted()
        val navDetailEvent = viewModel.navigateToSelectedMovie.getOrAwaitValue()
        assertThat(navDetailEvent, `is`(nullValue()))
    }
}