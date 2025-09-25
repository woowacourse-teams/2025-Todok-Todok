package com.team.todoktodok.compose.discussion.latest

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsScreen
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsUiState
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState
import com.team.todoktodok.presentation.compose.preview.LatestDiscussionsPreviewParameterProvider
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import java.time.LocalDateTime

class LatestDiscussionsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val previewData = LatestDiscussionsPreviewParameterProvider().values.first()
    private val discussion =
        LatestDiscussionsUiState(
            discussions =
                listOf(
                    DiscussionUiState(
                        Discussion(
                            id = 1L,
                            discussionTitle = "JPA 성능 최적화",
                            book = Book(1L, "자바 ORM 표준 JPA 프로그래밍", "김영한", ""),
                            writer = User(1L, Nickname("홍길동"), ""),
                            createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
                            discussionOpinion = "fetch join을 남발하면 안됩니다.",
                            likeCount = 0,
                            commentCount = 0,
                            isLikedByMe = false,
                        ),
                    ),
                ),
        )

    @Test
    fun `아이템이_화면에_보여진다`() {
        composeTestRule.setContent {
            LatestDiscussionsScreen(
                uiState = discussion,
                onLoadMore = {},
                onClick = {},
                isRefreshing = false,
                onRefresh = {},
            )
        }

        composeTestRule
            .onNodeWithText(discussion.discussions.first().bookTitle)
            .assertIsDisplayed()
    }

    @Test
    fun `토론방_클릭시_onClick이_호출된다`() {
        var clickedId: Long? = null

        composeTestRule.setContent {
            LatestDiscussionsScreen(
                uiState = previewData,
                onLoadMore = {},
                onClick = { id -> clickedId = id },
                isRefreshing = false,
                onRefresh = {},
            )
        }

        val firstItem = previewData.discussions.first()
        composeTestRule.onNodeWithText(previewData.discussions.first().bookTitle).performClick()

        Assertions.assertTrue(clickedId == firstItem.discussionId)
    }
}
