package com.team.ui_compose.compose.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.User
import com.team.ui_compose.component.DiscussionCard
import com.team.ui_compose.component.DiscussionCardType
import com.team.ui_compose.discussion.model.DiscussionUiModel
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class DiscussionCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val baseUiState =
        DiscussionUiModel(
            item =
                Discussion(
                    id = 1L,
                    discussionTitle = "JPA 성능 최적화",
                    book = Book(1L, "자바 ORM 표준 JPA 프로그래밍", "김영한", ""),
                    writer = User(1L, "홍길동", ""),
                    createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
                    discussionOpinion =
                        "응집도와 결합도가 어떤 차이를 가지는 지에 대한 다른 분들의 생각이 궁금합니다." +
                            "응집도는 내부에 얼마나 비슷한 책임들이 모여있는가. 얼마나 연관있는 멤버들이 똘똘 뭉쳐있는가",
                    likeCount = 12,
                    commentCount = 3,
                    viewCount = 10,
                    isLikedByMe = false,
                ),
        )

    @Test
    fun `기본_카드는_책_제목_저자_토론_제목_작성자_좋아요수_조회수_댓글수가_보인다`() {
        // when
        composeTestRule.setContent {
            DiscussionCard(
                uiState = baseUiState,
                discussionCardType = DiscussionCardType.Default,
            )
        }

        // then
        composeTestRule.onNodeWithText("자바 ORM 표준 JPA 프로그래밍").assertIsDisplayed()
        composeTestRule.onNodeWithText("김영한").assertIsDisplayed()
        composeTestRule.onNodeWithText("JPA 성능 최적화").assertIsDisplayed()
        composeTestRule.onNodeWithText("홍길동").assertIsDisplayed()
        composeTestRule.onNodeWithText("12").assertIsDisplayed()
        composeTestRule.onNodeWithText("3").assertIsDisplayed()
        composeTestRule.onNodeWithText("10").assertIsDisplayed()
    }

    @Test
    fun `작성자_숨김_카드는_작성자정보는_안보이고_책정보와_토론제목과_지표는_보인다`() {
        // when
        composeTestRule.setContent {
            DiscussionCard(
                uiState = baseUiState,
                discussionCardType = DiscussionCardType.WriterHidden,
            )
        }

        // then
        composeTestRule.onNodeWithText("홍길동").assertDoesNotExist()

        composeTestRule.onNodeWithText("자바 ORM 표준 JPA 프로그래밍").assertIsDisplayed()
        composeTestRule.onNodeWithText("김영한").assertIsDisplayed()
        composeTestRule.onNodeWithText("JPA 성능 최적화").assertIsDisplayed()
        composeTestRule.onNodeWithText("12").assertIsDisplayed()
        composeTestRule.onNodeWithText("3").assertIsDisplayed()
        composeTestRule.onNodeWithText("10").assertIsDisplayed()
    }

    @Test
    fun `검색_강조_카드는_검색어가_포함된_토론제목이_보인다`() {
        // when
        composeTestRule.setContent {
            DiscussionCard(
                uiState = baseUiState.copy(),
                discussionCardType = DiscussionCardType.QueryHighlighting("성능"),
            )
        }

        // then
        composeTestRule.onNodeWithText("JPA 성능 최적화").assertIsDisplayed()
        composeTestRule.onNodeWithText("자바 ORM 표준 JPA 프로그래밍").assertIsDisplayed()
        composeTestRule.onNodeWithText("김영한").assertIsDisplayed()
        composeTestRule.onNodeWithText("홍길동").assertIsDisplayed()
    }

    @Test
    fun `의견_노출_카드는_토론제목과_의견내용이_같이_보인다`() {
        // when
        composeTestRule.setContent {
            DiscussionCard(
                uiState = baseUiState,
                discussionCardType = DiscussionCardType.OpinionVisible,
            )
        }

        // then
        composeTestRule.onNodeWithText("자바 ORM 표준 JPA 프로그래밍").assertIsDisplayed()
        composeTestRule.onNodeWithText("김영한").assertIsDisplayed()
        composeTestRule.onNodeWithText("JPA 성능 최적화").assertIsDisplayed()
        composeTestRule.onNodeWithText("홍길동").assertIsDisplayed()
        composeTestRule
            .onNodeWithText("응집도와 결합도가 어떤 차이를 가지는 지에 대한 다른 분들의 생각이 궁금합니다.응집도는 내부에 얼마나 비슷한 책임들이 모여있는가. 얼마나 연관있는 멤버들이 똘똘 뭉쳐있는가")
            .assertIsDisplayed()
    }
}
