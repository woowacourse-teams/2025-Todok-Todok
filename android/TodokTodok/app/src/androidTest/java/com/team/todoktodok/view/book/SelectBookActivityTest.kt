package com.team.todoktodok.view.book

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.team.todoktodok.R
import com.team.todoktodok.presentation.view.book.SelectBookActivity
import org.junit.Before
import org.junit.Test

class SelectBookActivityTest {
    @Before
    fun setup() {
        val intent =
            SelectBookActivity.Intent(
                context = ApplicationProvider.getApplicationContext(),
            )
        ActivityScenario.launch<SelectBookActivity>(intent)
    }

    @Test
    fun shouldDisplayBackButton() {
        onView(withId(R.id.btn_back)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplaySelectBookTitle() {
        onView(withId(R.id.tv_select_book)).check(matches(withText(R.string.select_book)))
    }

    @Test
    fun shouldDisplaySearchField() {
        onView(withId(R.id.etl_search_keyword)).check(matches(isDisplayed()))
        onView(withId(R.id.et_search_keyword)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowSearchHintText() {
        onView(withId(R.id.et_search_keyword)).check(matches(withHint(R.string.select_book_hint_search_keyword)))
    }

    @Test
    fun shouldShowEmptySearchResultView() {
        onView(withId(R.id.iv_empty_search_result)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_empty_search_result_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_empty_search_result_subTitle)).check(matches(isDisplayed()))
    }
}
