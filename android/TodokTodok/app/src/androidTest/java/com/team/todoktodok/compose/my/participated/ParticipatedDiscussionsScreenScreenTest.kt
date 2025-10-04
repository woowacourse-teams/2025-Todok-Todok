package com.team.todoktodok.compose.my.participated

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState
import com.team.todoktodok.presentation.compose.my.participated.ParticipatedDiscussionsScreen
import com.team.todoktodok.presentation.compose.my.participated.ParticipatedDiscussionsUiState
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class ParticipatedDiscussionsScreenScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `참여한_토론방_목록이_보인다`() {
        // Given
        val discussions =
            ParticipatedDiscussionsUiState(
                listOf(
                    DiscussionUiState(
                        item =
                            Discussion(
                                id = 1L,
                                discussionTitle = "JPA 성능 최적화",
                                book = Book(1L, "자바 ORM 표준 JPA 프로그래밍", "김영한", ""),
                                writer = User(1L, Nickname("홍길동"), ""),
                                createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
                                discussionOpinion =
                                    "응집도와 결합도가 어떤 차이를 가지는 지에 대한 다른 분들의 생각이 궁금합니다." +
                                        "응집도는 내부에 얼마나 비슷한 책임들이 모여있는가. 얼마나 연관있는 멤버들이 똘똘 뭉쳐있는가" +
                                        "응집도는 내부에 얼마나 비슷한 책임들이 모여있는가. 얼마나 연관있는 멤버들이 똘똘 뭉쳐있는가",
                                likeCount = 0,
                                commentCount = 0,
                                viewCount = 0,
                                isLikedByMe = false,
                            ),
                    ),
                ),
            )

        // When
        composeTestRule.setContent {
            ParticipatedDiscussionsScreen(uiState = discussions)
        }

        // Then
        discussions.discussions.forEach { item ->
            composeTestRule.onNodeWithText(item.discussionTitle).assertIsDisplayed()
        }
    }
}
