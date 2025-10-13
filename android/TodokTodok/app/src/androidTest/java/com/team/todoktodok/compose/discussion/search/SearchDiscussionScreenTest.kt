package com.team.todoktodok.compose.discussion.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionScreen
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionsUiState
import com.team.todoktodok.presentation.compose.preview.SearchDiscussionsUiStatePreviewParameterProvider
import org.junit.Rule
import org.junit.Test

class SearchDiscussionScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `검색화면_아이템이_표시된다`() {
        // given
        val uiState = SearchDiscussionsUiStatePreviewParameterProvider().values.first()

        // when
        composeTestRule.setContent {
            SearchDiscussionScreen(
                uiState = uiState,
                onCompleteRemoveDiscussion = {},
                onCompleteModifyDiscussion = {},
            )
        }

        // then
        uiState.discussions.forEach { item ->
            composeTestRule.onNodeWithText(item.discussionTitle).assertIsDisplayed()
        }
    }

    @Test
    fun `검색화면_검색결과_없을_때_빈화면이_표시된다`() {
        // given
        val emptyState = SearchDiscussionsUiState(type = DiscussionCardType.QueryHighlighting("토론"))

        // when
        composeTestRule.setContent {
            SearchDiscussionScreen(
                uiState = emptyState,
                onCompleteRemoveDiscussion = {},
                onCompleteModifyDiscussion = {},
            )
        }

        // then
        composeTestRule.onNodeWithText("토론에 대한 검색결과가 없어요").assertIsDisplayed()
    }
}
