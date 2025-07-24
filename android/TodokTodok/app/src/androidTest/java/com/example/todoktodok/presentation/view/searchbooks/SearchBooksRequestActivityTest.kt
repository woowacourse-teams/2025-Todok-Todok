package com.example.todoktodok.presentation.view.searchbooks

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.todoktodok.R
import com.example.todoktodok.presentation.view.MainActivity
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Test

class SearchBooksActivityTest {
    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.menu_library)).perform(click())
        onView(withId(R.id.iv_library_navigation)).perform(click())
    }

    @Test
    fun showAppBarTitle() {
        onView(withId(R.id.tv_book_search_title)).check(
            matches(
                withText(
                    R.string.all_book_search,
                ),
            ),
        )
    }

    @Test
    fun showBackIconInAppBar() {
        onView(withId(R.id.iv_book_search_back)).check(matches(isDisplayed()))
    }

    @Test
    fun clickBackIconToNavigateLibraryActivity() {
        onView(withId(R.id.iv_book_search_back)).perform(click())
        onView(allOf(withId(R.id.tv_library_title), withText(R.string.all_library))).check(
            matches(
                isDisplayed(),
            ),
        )
    }

    @Test
    fun showSearchBar() {
        onView(withId(R.id.view_book_search_bar)).check(matches(isDisplayed()))
    }
}
