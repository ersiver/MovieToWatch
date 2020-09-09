package com.breiter.movietowatchapp.ui.screen.search

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.breiter.movietowatchapp.R
import com.breiter.movietowatchapp.ServiceLocator
import com.breiter.movietowatchapp.data.repository.IMovieRepository
import com.breiter.movietowatchapp.util.FakeRepository
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

@MediumTest
class SearchFragmentTest {

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
     *  Test to check, that when the query is set, then the
     *  list of movies with matching title is displayed.
     */
    @Test
    fun setQuery_displayMovieList(){
        launchFragmentInContainer<SearchFragment>(Bundle(), R.style.AppTheme)
        onView(withId(R.id.queryEditText)).perform(typeText("Joker"))
        onView(withId(R.id.searchedMovieList)).check(matches(hasDescendant(withText("Joker"))))
    }

    /**
     *  Test to check, that when the query is typed then
     *  input is displayed correctly in the edit_text.
     */
    @Test
    fun inputQuery_displayQuery() {
        launchFragmentInContainer<SearchFragment>(Bundle(), R.style.AppTheme)
        onView(withId(R.id.queryEditText)).perform(typeText("Joker"))
        onView(withId(R.id.queryEditText)).check(matches(withText("Joker")))
    }

    /**
     *  Test to check, that when clear_button is clicked the edit_text
     *  is empty and no movie is displayed in the movie list.
     */
    @Test
    fun deleteQuery() {
        launchFragmentInContainer<SearchFragment>(Bundle(), R.style.AppTheme)
        onView(withId(R.id.queryEditText)).perform(typeText("Joker"))
        onView(withId(R.id.clear_button)).perform(click())
        onView(withId(R.id.queryEditText)).check(matches(withText("")))
        onView(withText("Joker")).check(doesNotExist())
    }

    /**
     * Navigation test to check, that when a saved_movies
     * button is clicked, the SavedFragment is launched.
     */
    @Test
    fun clickSavedButton_NavigateToSavedFragment() {
        val scenario = launchFragmentInContainer<SearchFragment>(Bundle(), R.style.AppTheme)
        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }

        onView(withId(R.id.saved_movies)).perform(click())
        verify(navController).navigate(SearchFragmentDirections.actionSearchFragmentToSavedFragment())
    }
}
