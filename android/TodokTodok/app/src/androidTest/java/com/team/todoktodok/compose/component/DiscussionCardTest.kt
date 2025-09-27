package com.team.todoktodok.compose.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.presentation.compose.core.component.DiscussionCard
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class DiscussionCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val baseUiState =
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
                            "응집도는 내부에 얼마나 비슷한 책임들이 모여있는가. 얼마나 연관있는 멤버들이 똘똘 뭉쳐있는가",
                    likeCount = 12,
                    commentCount = 3,
                    viewCount = 10,
                    isLikedByMe = false,
                ),
            searchKeyword = "JPA",
        )

    @Test
    fun `기본_카드는_책_제목_저자_토론_제목_작성자_좋아요수_조회수_댓글수가_보인다`() {
        // when
        composeTestRule.setContent {
            DiscussionCard(
                uiState = baseUiState,
                onClick = {},
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
        composeTestRule.onNodeWithText("0").assertIsDisplayed() // viewCount
    }

    @Test
    fun `작성자_숨김_카드는_작성자정보는_안보이고_책정보와_토론제목과_지표는_보인다`() {
        // when
        composeTestRule.setContent {
            DiscussionCard(
                uiState = baseUiState,
                onClick = {},
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
        composeTestRule.onNodeWithText("0").assertIsDisplayed()
    }

    @Test
    fun `검색_강조_카드는_검색어가_포함된_토론제목이_보인다`() {
        // when
        composeTestRule.setContent {
            DiscussionCard(
                uiState = baseUiState.copy(searchKeyword = "성능"),
                onClick = {},
                discussionCardType = DiscussionCardType.QueryHighlighting,
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
                onClick = {},
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
