package com.breiter.movietowatchapp.ui.screen.saved

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.breiter.movietowatchapp.R
import com.breiter.movietowatchapp.ServiceLocator
import com.breiter.movietowatchapp.data.repository.IMovieRepository
import com.breiter.movietowatchapp.util.FakeRepository
import com.breiter.movietowatchapp.util.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class SavedFragmentTest {

    private lateinit var repository: IMovieRepository

    @Before
    fun setUp()  {
        repository = FakeRepository()
        ServiceLocator.repository = repository
    }

    @After
    fun tearDown() {
        ServiceLocator.resetRepository()
    }

    /**
     * Test to verify that all saved movies
     * are shown, when the fragment launches.
     */
    @Test
    fun showAllMovies() = runBlockingTest {
        val movie1 = TestUtil.createMovie(1, "TITLE1")
        val movie2 = TestUtil.createMovie(2, "TITLE2")
        val movie3 = TestUtil.createMovie(3, "TITLE3")

        repository.insert(movie1)
        repository.insert(movie2)
        repository.insert(movie3)

        launchFragmentInContainer<SavedFragment>(Bundle(), R.style.AppTheme)
        onView(withText("TITLE1")).check(matches(isDisplayed()))
        onView(withText("TITLE2")).check(matches(isDisplayed()))
        onView(withText("TITLE3")).check(matches(isDisplayed()))
    }

    /**
     * Navigation test to check, that when a movie item is clicked in
     * the movie list, it the correct movie is passed to DetailFragment.
     */
    @Test
    fun clickMovie_NavigateToDetailFragment() = runBlockingTest {
        val movie1 = TestUtil.createMovie(1, "TITLE1")
        val movie2 = TestUtil.createMovie(2,"TITLE2")
        repository.insert(movie1)
        repository.insert(movie2)

        val scenario = launchFragmentInContainer<SavedFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }

        onView(withId(R.id.saved_movies_list)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
            hasDescendant(withText("TITLE1")), click()))

        verify(navController).navigate(
            SavedFragmentDirections.actionSavedFragmentToDetailFragment(
                movie1
            )
        )
    }

    /**
     * Navigation test to check, that when a search_movie
     * button is clicked, the SearchFragment is launched.
     */
    @Test
    fun clickSearchButton_NavigateToSearchFragment() {
        val scenario = launchFragmentInContainer<SavedFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }

        onView(withId(R.id.search_button)).perform(click())
        verify(navController).navigate(SavedFragmentDirections.actionSavedFragmentToSearchFragment())
    }
}