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
import com.team.todoktodok.presentation.compose.my.participated.ParticipatedDiscussionsUiModel
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class ParticipatedDiscussionsScreenScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val uiModel =
        ParticipatedDiscussionsUiModel(
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
                DiscussionUiState(
                    item =
                        Discussion(
                            id = 2L,
                            discussionTitle = "코틀린 코루틴 완전 정복",
                            book = Book(2L, "Kotlin in Action", "Dmitry Jemerov", ""),
                            writer = User(2L, Nickname("박코루틴"), ""),
                            createAt = LocalDateTime.of(2025, 7, 13, 12, 0),
                            discussionOpinion = "suspend fun과 launch 차이를 이해해야 합니다.",
                            likeCount = 10,
                            viewCount = 0,
                            commentCount = 2,
                            isLikedByMe = true,
                        ),
                ),
                DiscussionUiState(
                    item =
                        Discussion(
                            id = 3L,
                            discussionTitle = "MVVM 구조 제대로 이해하기",
                            book = Book(3L, "안드로이드 아키텍처 가이드", "구글", ""),
                            writer = User(3L, Nickname("김아키텍처"), ""),
                            createAt = LocalDateTime.of(2025, 7, 14, 12, 0),
                            discussionOpinion = "UI와 로직을 분리해 유지보수가 쉬워집니다.",
                            likeCount = 3,
                            viewCount = 0,
                            commentCount = 1,
                            isLikedByMe = false,
                        ),
                ),
            ),
        )

    @Test
    fun `내 토론만 보기 버튼이이_보인다`() {
        // When
        composeTestRule.setContent {
            ParticipatedDiscussionsScreen(uiModel = uiModel, onChangeShowMyDiscussion = {})
        }

        // Then
        composeTestRule.onNodeWithText("내 글만 보기").assertIsDisplayed()
    }

    @Test
    fun `모든_참여한_토론방_목록이_보인다`() {
        // given
        val testUiModel = uiModel.copy(showMyDiscussion = false)

        // When
        composeTestRule.setContent {
            ParticipatedDiscussionsScreen(uiModel = testUiModel, onChangeShowMyDiscussion = {})
        }

        // Then
        composeTestRule.onNodeWithText("JPA 성능 최적화").assertIsDisplayed()
        composeTestRule.onNodeWithText("코틀린 코루틴 완전 정복").assertIsDisplayed()
        composeTestRule.onNodeWithText("MVVM 구조 제대로 이해하기").assertIsDisplayed()
    }

    @Test
    fun `내가_참여한_토론방_목록만_보인다`() {
        // given
        val testUiModel = uiModel.copy(showMyDiscussion = true, memberId = 1)

        // When
        composeTestRule.setContent {
            ParticipatedDiscussionsScreen(uiModel = testUiModel, onChangeShowMyDiscussion = {})
        }

        // Then
        composeTestRule.onNodeWithText("JPA 성능 최적화").assertIsDisplayed()
    }

    @Test
    fun `참여한 토론방이 없을경우 가이드 메시지가 보인다`() {
        // given
        val testUiModel = uiModel.copy(discussions = emptyList())

        // When
        composeTestRule.setContent {
            ParticipatedDiscussionsScreen(uiModel = testUiModel, onChangeShowMyDiscussion = {})
        }

        // Then
        composeTestRule.onNodeWithText("아직 참여한 토론방이 없어요 !").assertIsDisplayed()
        composeTestRule.onNodeWithText("나만의 주제로 토론방을 생성해보세요").assertIsDisplayed()
        composeTestRule.onNodeWithText("토론방 생성하기 >").assertIsDisplayed()
    }
}
