package com.team.ui_xml.discussiondetail.comments.vm

import androidx.lifecycle.SavedStateHandle
import com.team.domain.model.LikeStatus
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.TokenRepository
import com.team.ui_xml.CoroutinesTestExtension
import com.team.ui_xml.InstantTaskExecutorExtension
import com.team.ui_xml.discussiondetail.comments.CommentsUiEvent
import com.team.ui_xml.discussiondetail.comments.vm.CommentsViewModel
import com.team.ui_xml.ext.getOrAwaitValue
import com.team.ui_xml.fixture.COMMENTS_FIXTURE
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class CommentsViewModelTest {
    private lateinit var commentsViewModel: CommentsViewModel
    private lateinit var commentRepository: CommentRepository
    private lateinit var tokenRepository: TokenRepository

    @BeforeEach
    fun setUp() {
        // given
        commentRepository = mockk(relaxed = true)
        tokenRepository = mockk(relaxed = true)
        coEvery { tokenRepository.getMemberId() } returns ME
        coEvery { commentRepository.getCommentsByDiscussionId(DISCUSSION_ID) } returns
            NetworkResult.Success(COMMENTS_FIXTURE)

        // when
        val state = SavedStateHandle(mapOf(CommentsViewModel.KEY_DISCUSSION_ID to DISCUSSION_ID))
        commentsViewModel = CommentsViewModel(state, commentRepository, tokenRepository)

        // then (생성 완료가 준비 상태)
    }

    @Test
    fun `초기 로드 성공시 comments 채워지고 isLoading=false`() =
        runTest {
            // when
            val state = commentsViewModel.uiState.getOrAwaitValue()
            advanceUntilIdle()

            // then
            assertFalse(state.isLoading)
            assertThat(state.comments.map { it.comment.id }).containsExactly(1L, 2L, 3L, 4L, 5L)
        }

    @Test
    fun `showNewComment는 목록 재로딩 후 이벤트 발생`() =
        runTest {
            // when
            commentsViewModel.showNewComment()
            val event = commentsViewModel.uiEvent.getOrAwaitValue()

            // then
            assertThat(event).isEqualTo(CommentsUiEvent.ShowNewComment)
        }

    @Test
    fun `toggleLike 성공 시 해당 코멘트만 재로딩되어 갱신`() =
        runTest {
            // given
            coEvery { commentRepository.toggleLike(DISCUSSION_ID, 1L) } returns
                NetworkResult.Success(LikeStatus.LIKE)
            val updatedComment = COMMENTS_FIXTURE[0].copy(isLikedByMe = true, likeCount = 1)
            coEvery { commentRepository.getComment(DISCUSSION_ID, 1L) } returns
                NetworkResult.Success(updatedComment)

            // when
            commentsViewModel.toggleLike(1L)
            advanceUntilIdle()
            val state = commentsViewModel.uiState.getOrAwaitValue()

            // then
            val updated = state.comments.first { it.comment.id == 1L }.comment
            assertTrue(updated.isLikedByMe)
            assertThat(updated.likeCount).isEqualTo(1)
            val untouched = state.comments.first { it.comment.id == 2L }.comment
            assertFalse(untouched.isLikedByMe)
        }

    @Test
    fun `deleteComment 성공 시 목록 재로딩되고 DeleteComment 이벤트 발생`() =
        runTest {
            // given
            coEvery {
                commentRepository.deleteComment(
                    DISCUSSION_ID,
                    2L,
                )
            } returns NetworkResult.Success(Unit)
            coEvery { commentRepository.getCommentsByDiscussionId(DISCUSSION_ID) } returns
                NetworkResult.Success(COMMENTS_FIXTURE.minus(COMMENTS_FIXTURE[1]))

            // when
            commentsViewModel.deleteComment(2L)
            advanceUntilIdle()
            val event = commentsViewModel.uiEvent.getOrAwaitValue()
            val state = commentsViewModel.uiState.getOrAwaitValue()

            // then
            assertThat(event).isEqualTo(CommentsUiEvent.DeleteComment)
            assertThat(state.comments.map { it.comment.id }).containsExactly(1L, 3L, 4L, 5L)
        }

    @Test
    fun `updateComment는 ShowCommentUpdate 이벤트 발생`() =
        runTest {
            // given
            val expected = CommentsUiEvent.ShowCommentUpdate(DISCUSSION_ID, 1L, "edit")

            // when
            commentsViewModel.updateComment(commentId = 1L, content = "edit")
            advanceUntilIdle()
            val event = commentsViewModel.uiEvent.getOrAwaitValue()

            // then
            assertThat(event).isEqualTo(expected)
        }

    @Test
    fun `report 성공 시 성공 메시지 이벤트`() =
        runTest {
            // given
            coEvery {
                commentRepository.report(
                    DISCUSSION_ID,
                    2L,
                    any(),
                )
            } returns NetworkResult.Success(Unit)

            // when
            commentsViewModel.reportComment(2L, "스팸")
            val event = commentsViewModel.uiEvent.getOrAwaitValue()
            advanceUntilIdle()

            // then
            assertThat(event).isEqualTo(CommentsUiEvent.ShowReportCommentSuccessMessage)
        }

    @Test
    fun `report 실패 시 에러 이벤트와 isLoading=false`() =
        runTest {
            // given
            val exception = TodokTodokExceptions.EmptyBodyException
            coEvery { commentRepository.report(DISCUSSION_ID, 2L, any()) } returns
                NetworkResult.Failure(exception)

            // when
            commentsViewModel.reportComment(2L, "사유")
            val observed = async { commentsViewModel.uiEvent.getOrAwaitValue() }

            // then
            assertThat(observed.await()).isEqualTo(CommentsUiEvent.ShowError(exception))
            assertFalse(commentsViewModel.uiState.getOrAwaitValue().isLoading)
        }

    @Test
    fun `showCommentCreate는 현재 입력값과 함께 이벤트 발생`() =
        runTest {
            // given
            commentsViewModel.updateCommentContent("hello")

            // when
            commentsViewModel.showCommentCreate()
            val event = commentsViewModel.uiEvent.getOrAwaitValue()
            advanceUntilIdle()

            // then
            assertThat(event).isEqualTo(
                CommentsUiEvent.ShowCommentCreate(discussionId = DISCUSSION_ID, content = "hello"),
            )
        }

    @Test
    fun `updateCommentContent는 uiState commentContent를 갱신`() =
        runTest {
            // when
            commentsViewModel.updateCommentContent("abc")
            val state = commentsViewModel.uiState.getOrAwaitValue()

            // then
            assertThat(state.commentContent).isEqualTo("abc")
        }

    @Test
    fun `초기 로드 실패 시 에러 이벤트와 isLoading=false`() =
        runTest {
            // given
            val exception = TodokTodokExceptions.EmptyBodyException
            coEvery { commentRepository.getCommentsByDiscussionId(DISCUSSION_ID) } returns
                NetworkResult.Failure(exception)

            // when
            val state =
                SavedStateHandle(mapOf(CommentsViewModel.KEY_DISCUSSION_ID to DISCUSSION_ID))
            val failedViewModel = CommentsViewModel(state, commentRepository, tokenRepository)
            val event = failedViewModel.uiEvent.getOrAwaitValue()
            advanceUntilIdle()

            // then
            assertThat(event).isEqualTo(CommentsUiEvent.ShowError(exception))
            assertFalse(failedViewModel.uiState.getOrAwaitValue().isLoading)
        }

    companion object {
        private const val DISCUSSION_ID = 10L
        private const val ME = 100L
    }
}
