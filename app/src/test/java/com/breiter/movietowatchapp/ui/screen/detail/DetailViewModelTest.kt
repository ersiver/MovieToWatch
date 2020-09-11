package com.breiter.movietowatchapp.ui.screen.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.breiter.movietowatchapp.ServiceLocator
import com.breiter.movietowatchapp.util.FakeRepository
import com.breiter.movietowatchapp.util.MainCoroutineRule
import com.breiter.movietowatchapp.util.TestUtil
import com.breiter.movietowatchapp.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Local unit tests for the implementation of [DetailViewModel]
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
    fun start_movieValueSet() {
        val movie = TestUtil.createMovie(1, "TITLE1")
        viewModel.start(movie)
        val movieData = viewModel.selectedMovie.getOrAwaitValue()
        assertThat(movieData, `is`(movie))
        assertThat(movieData.title, `is`("TITLE1"))
        assertThat(movieData.id, `is`(1))
    }


    @Test
    fun clickAddRemoveOnUnsaved_saveMovie() = runBlockingTest {
        val movie = TestUtil.createMovie(1, "TITLE1")

        viewModel.start(movie)
        var isSavedValue = viewModel.isSaved.getOrAwaitValue()
        assertThat(isSavedValue, `is`(false))

        viewModel.onAddRemoveClick()
        isSavedValue = viewModel.isSaved.getOrAwaitValue()
        assertThat(isSavedValue, `is`(true))
    }

    @Test
    fun clickAddRemoveOnSaved_removeMovie() = runBlockingTest {
        val movie = TestUtil.createMovie()
        repository.insert(movie)

        viewModel.start(movie)
        var isSavedValue = viewModel.isSaved.getOrAwaitValue()
        assertThat(isSavedValue, `is`(true))

        viewModel.onAddRemoveClick()
        isSavedValue = viewModel.isSaved.getOrAwaitValue()
        assertThat(isSavedValue, `is`(false))
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
    fun onSaveClick_navigateToSearchTrue() {
        viewModel.onSavedClicked()
        val navSaveEvent = viewModel.navigateToSaved.getOrAwaitValue()
        assertThat(navSaveEvent, `is`(true))
    }

    @Test
    fun navigateToSearchComplete_navigateToSaveNull() {
        viewModel.navigateToSavedCompleted()
        val navSaveEvent = viewModel.navigateToSaved
        assertThat(navSaveEvent.getOrAwaitValue(), `is`(nullValue()))
    }

    @Test
    fun addToSaved_saveValueTrue() = runBlockingTest {
        val movie = TestUtil.createMovie(1, "TITLE1")
        viewModel.start(movie)
        var isSavedValue = viewModel.isSaved.getOrAwaitValue()
        assertThat(isSavedValue, `is`(false))

        viewModel.saveMovie()
        isSavedValue = viewModel.isSaved.getOrAwaitValue()
        assertThat(isSavedValue, `is`(true))
    }
}
