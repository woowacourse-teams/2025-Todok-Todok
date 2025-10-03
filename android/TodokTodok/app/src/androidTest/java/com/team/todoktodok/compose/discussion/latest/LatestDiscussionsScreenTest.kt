package com.team.todoktodok.compose.discussion.latest

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.PageInfo
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsScreen
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsUiState
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState
import com.team.todoktodok.presentation.compose.preview.LatestDiscussionsPreviewParameterProvider
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class LatestDiscussionsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val previewData = LatestDiscussionsPreviewParameterProvider().values.first()

    @OptIn(ExperimentalMaterial3Api::class)
    private fun setContent(
        uiState: LatestDiscussionsUiState,
        isLoading: Boolean = false,
        onLoadMore: () -> Unit = {},
        onRefresh: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            LatestDiscussionsScreen(
                uiState = uiState,
                isLoading = isLoading,
                snackbarHostState = SnackbarHostState(),
                pullToRefreshState = rememberPullToRefreshState(),
                onLoadMore = onLoadMore,
                onRefresh = onRefresh,
            )
        }
    }

    @Test
    fun `아이템이_화면에_보여진다`() {
        val uiState =
            LatestDiscussionsUiState(
                discussions =
                    listOf(
                        DiscussionUiState(
                            item =
                                Discussion(
                                    id = 1L,
                                    discussionTitle = "JPA 성능 최적화",
                                    book = Book(1L, "자바 ORM 표준 JPA 프로그래밍", "김영한", ""),
                                    writer = User(1L, Nickname("정페토"), ""),
                                    createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
                                    discussionOpinion = "응집도와 결합도가 어떤 차이를 가지는 지에 대한 다른 분들의 생각이 궁금합니다.",
                                    likeCount = 95,
                                    commentCount = 96,
                                    viewCount = 97,
                                    isLikedByMe = false,
                                ),
                        ),
                    ),
                isRefreshing = false,
            )

        setContent(uiState)

        composeTestRule.onNodeWithText("JPA 성능 최적화").assertIsDisplayed()
        composeTestRule.onNodeWithText("자바 ORM 표준 JPA 프로그래밍").assertIsDisplayed()
        composeTestRule.onNodeWithText("김영한").assertIsDisplayed()
        composeTestRule.onNodeWithText("정페토").assertIsDisplayed()
        composeTestRule.onNodeWithText("95").assertIsDisplayed()
        composeTestRule.onNodeWithText("96").assertIsDisplayed()
        composeTestRule.onNodeWithText("97").assertIsDisplayed()
        composeTestRule.onNodeWithText("응집도와 결합도가 어떤 차이를 가지는 지").assertIsNotDisplayed()
    }

    @Test
    fun `로딩중이면_ProgressBar가_표시된다`() {
        val uiState =
            LatestDiscussionsUiState(
                discussions = emptyList(),
            )

        setContent(uiState, isLoading = true)

        composeTestRule
            .onNodeWithContentDescription("로딩 중")
            .assertIsDisplayed()
    }

    @Test
    fun `마지막페이지면_안내문구가_표시된다`() {
        val uiState =
            LatestDiscussionsUiState(
                discussions = emptyList(),
                latestPage =
                    PageInfo(
                        hasNext = false,
                        nextCursor = null,
                    ),
            )

        setContent(uiState)

        composeTestRule.onNodeWithText("마지막 토론이에요").assertIsDisplayed()
    }
}
