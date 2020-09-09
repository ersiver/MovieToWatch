package com.breiter.movietowatchapp.ui.screen.saved

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.breiter.movietowatchapp.ServiceLocator
import com.breiter.movietowatchapp.ui.screen.detail.DetailViewModel
import com.breiter.movietowatchapp.util.FakeRepository
import com.breiter.movietowatchapp.util.MainCoroutineRule
import com.breiter.movietowatchapp.util.TestUtil
import com.breiter.movietowatchapp.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import retrofit2.http.GET

/**
 * Unit tests for the implementation of [SavedViewModel]
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
        viewModel = SavedViewModel(repository)
    }

    @After
    fun tearDown() {
        ServiceLocator.resetRepository()
    }


    @Test
    fun start_loadSavedMovies() = runBlockingTest {
    }


    @Test
    fun deleteMovie() {
    }

    @Test
    fun onSearchClick() {
    }

    @Test
    fun navigateToSearchComplete() {
    }

    @Test
    fun displayMovieDetails() {
    }

    @Test
    fun navigateToDetailsCompleted() {
    }

}
