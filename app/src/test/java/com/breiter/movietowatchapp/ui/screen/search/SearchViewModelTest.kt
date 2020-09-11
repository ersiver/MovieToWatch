package com.breiter.movietowatchapp.ui.screen.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.breiter.movietowatchapp.ServiceLocator
import com.breiter.movietowatchapp.util.FakeRepository
import com.breiter.movietowatchapp.util.MainCoroutineRule
import com.breiter.movietowatchapp.util.TestUtil
import com.breiter.movietowatchapp.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

/**
 * Local unit test for the implementation of [SearchViewModel]
 */
@ExperimentalCoroutinesApi
class SearchViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: SearchViewModel
    private lateinit var repository: FakeRepository

    @Before
    fun setUp() {
        repository = FakeRepository()
        ServiceLocator.repository = repository
        viewModel = SearchViewModel(repository)
    }

    @After
    fun tearDown() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun onQueryTextChanged_queryValueChanged() {
        viewModel.onQueryTextChanged("TITLE1", 1, 1, 1)
        val queryValue = viewModel.query.getOrAwaitValue()
        assertThat(queryValue, `is`("TITLE1"))
    }

    @Test
    fun onQueryTextChanged_clearQueryFalse() {
        viewModel.onQueryTextChanged("TITLE1", 1, 1, 1)
        val clearQueryValue = viewModel.clearQuery.getOrAwaitValue()
        assertThat(clearQueryValue, `is`(false))
    }

    @Test
    fun onQueryTextChanged_movieListChanged() {
        viewModel.onQueryTextChanged("TITLE1", 1, 1, 1)
        var searchedMoviesValue = viewModel.searchedMovies.getOrAwaitValue()
        assertThat(searchedMoviesValue[0].title, `is`("TITLE1"))

        viewModel.onQueryTextChanged("TITLE2", 1, 1, 1)
        searchedMoviesValue = viewModel.searchedMovies.getOrAwaitValue()
        assertThat(searchedMoviesValue[0].title, `is`("TITLE2"))
    }

    @Test
    fun onQueryTextChanged_networkStatusTriggered() {
        viewModel.onQueryTextChanged("TITLE1", 1, 1, 1)
        val statusValue = viewModel.status.getOrAwaitValue()
        assertThat(statusValue, `is`(NetworkStatus.DONE))
    }

    @Test
    fun onClearClicked_clearQueryTrue() {
        viewModel.onClearClicked()
        val clearQueryValue = viewModel.clearQuery.getOrAwaitValue()
        assertThat(clearQueryValue, `is`(true))
    }

    @Test
    fun displayMovieDetails_navigateMatchingMovie() {
        val movie = TestUtil.createMovie()
        viewModel.displayMovieDetails(movie)
        val navToDetailValue = viewModel.navigateToSelectedMovie.getOrAwaitValue()
        assertThat(navToDetailValue, `is` (movie))
    }

    @Test
    fun navigateToDetailsCompleted_navigateNull() {
        viewModel.navigateToDetailsCompleted()
        val navToDetailValue = viewModel.navigateToSelectedMovie
        assertThat(navToDetailValue.getOrAwaitValue(), `is` (nullValue()))
    }

    @Test
    fun onSavedClicked_navigateToSaveTrue() {
        viewModel.onSavedClicked()
        val navToSavedValue = viewModel.navigateToSaved.getOrAwaitValue()
        assertThat(navToSavedValue, `is`(true))
    }

    @Test
    fun navigateToSavedCompleted_navigateToSaveNull() {
        viewModel.navigateToSavedCompleted()
        val navToSavedValue = viewModel.navigateToSaved
        assertThat(navToSavedValue.getOrAwaitValue(), `is`(nullValue()))
    }
}