package com.breiter.movietowatchapp.ui.screen.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.breiter.movietowatchapp.ServiceLocator
import com.breiter.movietowatchapp.util.FakeRepository
import com.breiter.movietowatchapp.util.MainCoroutineRule
import com.breiter.movietowatchapp.util.TestUtil
import com.breiter.movietowatchapp.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the implementation of [DetailViewModel]
 */
@ExperimentalCoroutinesApi
class DetailViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DetailViewModel
    private lateinit var repository: FakeRepository

    @Before
    fun setUp() {
        repository = FakeRepository()
        ServiceLocator.repository = repository
        viewModel = DetailViewModel(repository)
    }

    @After
    fun tearDown() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun start_setInitialMovieValue() {
        val movie = TestUtil.createMovie(1, "TITLE1")
        viewModel.start(movie)
        val movieData = viewModel.selectedMovie.getOrAwaitValue()
        assertThat(movieData, `is`(movie))
        assertThat(movieData.title, `is`("TITLE1"))
        assertThat(movieData.id, `is`(1))
    }

    @Test
    fun notSavedMovie_valueFalse() = runBlocking {
        val movie = TestUtil.createMovie(1, "TITLE1")
        viewModel.initSavedStatus(movie)
        val initialSave = viewModel.isSaved.getOrAwaitValue()
        assertThat(initialSave, `is`(false))
    }

    @Test
    fun savedMovie_valueTrue() = runBlocking {
        val movie = TestUtil.createMovie(1, "TITLE1")
        repository.insert(movie)
        viewModel.initSavedStatus(movie)
        val initialSave = viewModel.isSaved.getOrAwaitValue()
        assertThat(initialSave, `is`(true))
    }

    @Test
    fun onSearchNavClicked_navigationEventTriggered() {
        viewModel.onSearchClick()
        val isEventTriggered = viewModel.navigateToSearch.getOrAwaitValue()
        assertThat(isEventTriggered, `is` (true))
    }

    @Test
    fun navigateToSearchComplete() {
        viewModel.navigateToSearchComplete()
        val isEventTriggered = viewModel.navigateToSearch
        assertThat(isEventTriggered.getOrAwaitValue(), `is` (nullValue()))
    }

    @Test
    fun onSaveNavClicked_navigationEventTriggered() {
        viewModel.onSavedClicked()
        val isEventTriggered = viewModel.navigateToSaved.getOrAwaitValue()
        assertThat(isEventTriggered, `is`(true))
    }

    @Test
    fun navigateToSavedCompleted() {
        viewModel.navigateToSavedCompleted()
        val isEventTriggered = viewModel.navigateToSaved
        assertThat(isEventTriggered.getOrAwaitValue(), `is` (nullValue()))
    }
}