package com.breiter.movietowatchapp.ui.screen.detail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.breiter.movietowatchapp.R
import com.breiter.movietowatchapp.ServiceLocator
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.repository.IMovieRepository
import com.breiter.movietowatchapp.util.FakeRepository
import com.breiter.movietowatchapp.util.TestUtil
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

/**
 * Instrumental test for the DetailFragment.
 */

@MediumTest
@RunWith(AndroidJUnit4::class)
class DetailFragmentTest {
    private lateinit var repository: IMovieRepository
    private lateinit var movie: Movie

    @Before
    fun setUp() {
        repository = FakeRepository()
        ServiceLocator.repository = repository
        movie = TestUtil.createMovie()
    }

    @After
    fun tearDown() {
        ServiceLocator.resetRepository()
    }

    /**
     * Test to check, whether the movie details are displayed
     *  on the screen when the DetailFragment is launched.
     */
    @Test
    fun launchFragment_displayedInUi() {
        val bundle = DetailFragmentArgs(movie).toBundle()
        launchFragmentInContainer<DetailFragment>(bundle, R.style.AppTheme)

        onView(withId(R.id.movie_title_text)).check(matches(isDisplayed()))
        onView(withId(R.id.movie_title_text)).check(matches(withText(movie.title)))
        onView(withId(R.id.poster_image)).check(matches(isDisplayed()))
        onView(withId(R.id.overview_text)).check(matches(withText(movie.overview)))
        onView(withId(R.id.overview_text)).check(matches(isDisplayed()))
        onView(withId(R.id.rating_text)).check(matches(withText(movie.rating.toString())))
        onView(withId(R.id.rating_text)).check(matches(isDisplayed()))
        onView(withId(R.id.releasedTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.releasedTextView)).check(matches(withText(movie.releaseDate)))
        onView(withId(R.id.language_text)).check(matches(withText(movie.language)))
        onView(withId(R.id.language_text)).check(matches(withText(movie.language)))
    }

    /**
     * Navigation test to check, that if nav_search button
     * is clicked the SearchFragment is launched.
     */
    @Test
    fun clickSearchButton_NavigateToSearch() {
        val bundle = DetailFragmentArgs(movie).toBundle()
        val scenario = launchFragmentInContainer<DetailFragment>(bundle, R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }

        onView(withId(R.id.action_nav_search)).perform(click())
        verify(navController).navigate(DetailFragmentDirections.actionDetailFragmentToSearchFragment())
    }

    /**
     * Navigation test to check, that if saved_movies button
     * is clicked the SaveFragment is launched.
     */
    @Test
    fun clickSavedMoviesButton_NavigateToSave() {
        val bundle = DetailFragmentArgs(movie).toBundle()
        val scenario = launchFragmentInContainer<DetailFragment>(bundle, R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }

        onView(withId(R.id.saved_movies)).perform(click())
        verify(navController).navigate(DetailFragmentDirections.actionDetailFragmentToSavedFragment())
    }
}