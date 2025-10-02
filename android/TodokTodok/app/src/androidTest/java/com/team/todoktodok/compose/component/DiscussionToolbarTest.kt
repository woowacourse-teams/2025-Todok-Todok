package com.team.todoktodok.compose.component

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.discussion.component.DiscussionToolbar
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionsUiState
import org.junit.Rule
import org.junit.Test

class DiscussionToolbarTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `검색 아이콘이 보인다`() {
        composeTestRule.setContent {
            DiscussionToolbar(
                defaultDiscussionsUiState = DiscussionsUiState(),
                isExistNotification = false,
                onSearch = {},
                onKeywordChange = {},
                onChangeSearchBarVisibility = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.content_description_discussions_toolbar_search),
            ).assertIsDisplayed()
    }

    @Test
    fun `알람 아이콘이 화면에 보이고 확인하지 않은 알람이 없으면 뱃지가 보이지 않는다`() {
        composeTestRule.setContent {
            DiscussionToolbar(
                defaultDiscussionsUiState = DiscussionsUiState(),
                isExistNotification = false,
                onSearch = {},
                onKeywordChange = {},
                onChangeSearchBarVisibility = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.content_description_discussions_toolbar_notification),
            ).assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.content_description_discussions_toolbar_has_notification),
            ).assertIsNotDisplayed()
    }

    @Test
    fun `확인하지 않은 알람이 있으면 뱃지가 보인다`() {
        composeTestRule.setContent {
            DiscussionToolbar(
                defaultDiscussionsUiState = DiscussionsUiState(),
                isExistNotification = true,
                onSearch = {},
                onKeywordChange = {},
                onChangeSearchBarVisibility = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.content_description_discussions_toolbar_has_notification),
            ).assertIsDisplayed()
    }

    @Test
    fun `검색 바가 보이면 검색 취소 텍스트가 보인다`() {
        composeTestRule.setContent {
            DiscussionToolbar(
                defaultDiscussionsUiState = DiscussionsUiState(searchBarVisible = true),
                isExistNotification = false,
                onSearch = {},
                onKeywordChange = {},
                onChangeSearchBarVisibility = {},
            )
        }

        composeTestRule
            .onNodeWithText("취소")
            .assertIsDisplayed()
    }
}
