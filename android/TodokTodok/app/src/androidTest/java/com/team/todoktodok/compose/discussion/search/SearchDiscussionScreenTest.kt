package com.team.todoktodok.compose.discussion.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionScreen
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionsUiState
import com.team.todoktodok.presentation.compose.preview.SearchDiscussionsUiStatePreviewParameterProvider
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SearchDiscussionScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `검색화면_아이템이_표시된다`() {
        // given
        val state = SearchDiscussionsUiStatePreviewParameterProvider().values.first()

        // when
        composeTestRule.setContent {
            SearchDiscussionScreen(
                uiState = state,
                onClick = {},
            )
        }

        // then
        state.discussions.forEach { item ->
            composeTestRule.onNodeWithText(item.discussionTitle).assertIsDisplayed()
        }
    }

    @Test
    fun `검색화면_아이템_클릭시_콜백이_호출된다`() {
        // given
        val sampleState = SearchDiscussionsUiStatePreviewParameterProvider().values.first()
        var clickedId: Long? = null

        // when
        composeTestRule.setContent {
            SearchDiscussionScreen(
                uiState = sampleState,
                onClick = { clickedId = it },
            )
        }

        val firstItem = sampleState.discussions.first()
        composeTestRule.onNodeWithText(firstItem.bookTitle).performClick()

        // then
        assertEquals(clickedId, firstItem.discussionId)
    }

    @Test
    fun `검색화면_검색결과_없을_때_빈화면이_표시된다`() {
        // given
        val emptyState = SearchDiscussionsUiState(type = DiscussionCardType.QueryHighlighting("토론"))

        // when
        composeTestRule.setContent {
            SearchDiscussionScreen(
                uiState = emptyState,
                onClick = {},
            )
        }

        // then
        composeTestRule.onNodeWithText("토론에 대한 검색결과가 없어요").assertIsDisplayed()
    }
}
