package com.team.todoktodok.presentation.vm

import androidx.lifecycle.SavedStateHandle
import com.team.domain.model.Comment
import com.team.domain.model.LikeStatus
import com.team.domain.model.Reply
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.model.member.User
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.ReplyRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.CommentDetailUiEvent
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.vm.CommentDetailViewModel
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.vm.CommentDetailViewModel.Companion.KEY_COMMENT_ID
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.vm.CommentDetailViewModel.Companion.KEY_DISCUSSION_ID
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
@ExtendWith(CoroutinesTestExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class CommentDetailViewModelTest {
    private lateinit var commentRepository: CommentRepository
    private lateinit var replyRepository: ReplyRepository
    private lateinit var tokenRepository: TokenRepository

    private lateinit var commentDetailViewModel: CommentDetailViewModel

    private fun newVm(
        discussionId: Long = DISCUSSION_ID,
        commentId: Long = COMMENT_ID,
    ) {
        val state =
            SavedStateHandle(
                mapOf(
                    KEY_DISCUSSION_ID to discussionId,
                    KEY_COMMENT_ID to commentId,
                ),
            )
        commentDetailViewModel =
            CommentDetailViewModel(state, commentRepository, replyRepository, tokenRepository)
    }

    @BeforeEach
    fun setup() {
        commentRepository = mockk(relaxed = true)
        replyRepository = mockk(relaxed = true)
        tokenRepository = mockk(relaxed = true)
        coEvery { tokenRepository.getMemberId() } returns ME
    }

    @Test
    fun `init 성공 - 코멘트와 리플 불러오고 isLoading false`() =
        runTest {
            val writerMe = mockk<User>(relaxed = true).apply { every { id } returns ME }
            val comment = mockk<Comment>(relaxed = true).apply { every { writer } returns writerMe }

            val replyWriterMe = mockk<User>(relaxed = true).apply { every { id } returns ME }
            val replyWriterOther = mockk<User>(relaxed = true).apply { every { id } returns OTHER }
            val replyMe =
                mockk<Reply>(relaxed = true).apply { every { writer } returns replyWriterMe }
            val replyOther =
                mockk<Reply>(relaxed = true).apply { every { writer } returns replyWriterOther }

            coEvery {
                commentRepository.getComment(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(comment)
            coEvery {
                replyRepository.getReplies(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(listOf(replyMe, replyOther))

            newVm(DISCUSSION_ID, COMMENT_ID)
            advanceUntilIdle()

            // then
            val ui = commentDetailViewModel.uiState.getOrAwaitValue()
            assertFalse(ui.isLoading)
            assertNotNull(ui.comment)
            assertEquals(2, ui.replies.size)
            assertTrue(ui.comment!!.isMyComment)
            assertTrue(ui.replies[0].isMyReply)
            assertFalse(ui.replies[1].isMyReply)
        }

    @Test
    fun `init 실패(댓글 로드 실패) - 에러 이벤트 방출, isLoading false`() =
        runTest {
            // given
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery {
                commentRepository.getComment(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Failure(ex)
            coEvery {
                replyRepository.getReplies(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(emptyList())

            newVm(DISCUSSION_ID, COMMENT_ID)
            val eventJob = async { commentDetailViewModel.uiEvent.getOrAwaitValue() }
            advanceUntilIdle()

            // then
            assertEquals(CommentDetailUiEvent.ShowError(ex), eventJob.await())
            assertFalse(commentDetailViewModel.uiState.getOrAwaitValue().isLoading)
        }

    @Test
    fun `reloadComment - 두 리포지토리 재호출, 새 댓글 이벤트(ShowNewReply) 발행`() =
        runTest {
            // given
            val writerMe = mockk<User>(relaxed = true).apply { every { id } returns ME }
            val comment = mockk<Comment>(relaxed = true).apply { every { writer } returns writerMe }
            val replyWriterOther = mockk<User>(relaxed = true).apply { every { id } returns OTHER }
            val replyOther =
                mockk<Reply>(relaxed = true).apply { every { writer } returns replyWriterOther }

            coEvery {
                commentRepository.getComment(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(comment)
            coEvery {
                replyRepository.getReplies(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(listOf(replyOther))

            newVm(DISCUSSION_ID, COMMENT_ID)
            advanceUntilIdle()

            // when
            val ev = async { commentDetailViewModel.uiEvent.getOrAwaitValue() }
            commentDetailViewModel.reloadComment()
            advanceUntilIdle()

            // then
            coVerify(exactly = 2) { commentRepository.getComment(DISCUSSION_ID, COMMENT_ID) }
            coVerify(exactly = 2) { replyRepository.getReplies(DISCUSSION_ID, COMMENT_ID) }
            assertEquals(CommentDetailUiEvent.ShowNewReply, ev.await())
        }

    @Test
    fun `updateContent - 입력값이 uiState에 반영`() =
        runTest {
            // given
            val writerMe = mockk<User>(relaxed = true).apply { every { id } returns ME }
            val comment = mockk<Comment>(relaxed = true).apply { every { writer } returns writerMe }
            coEvery {
                commentRepository.getComment(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(comment)
            coEvery {
                replyRepository.getReplies(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(emptyList())

            newVm(DISCUSSION_ID, COMMENT_ID)

            // when
            commentDetailViewModel.updateContent("hello")

            // then
            assertEquals("hello", commentDetailViewModel.uiState.getOrAwaitValue().content)
        }

    @Test
    fun `toggleCommentLike 성공 - 토글 API 호출 후 댓글 재로딩, 이벤트 방출`() =
        runTest {
            // given
            val writerMe = mockk<User>(relaxed = true).apply { every { id } returns ME }
            val comment = mockk<Comment>(relaxed = true).apply { every { writer } returns writerMe }
            coEvery {
                commentRepository.getComment(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(comment)
            coEvery {
                replyRepository.getReplies(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(emptyList())
            coEvery {
                commentRepository.toggleLike(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(LikeStatus.LIKE)

            newVm(DISCUSSION_ID, COMMENT_ID)

            advanceUntilIdle()

            val ev = async { commentDetailViewModel.uiEvent.getOrAwaitValue() }

            // when
            commentDetailViewModel.toggleCommentLike()
            advanceUntilIdle()

            // then
            coVerify(exactly = 1) { commentRepository.toggleLike(DISCUSSION_ID, COMMENT_ID) }
            coVerify(exactly = 2) { commentRepository.getComment(DISCUSSION_ID, COMMENT_ID) }
            assertEquals(CommentDetailUiEvent.ToggleCommentLike, ev.await())
        }

    @Test
    fun `deleteReply 성공 - 삭제 후 replies 재로딩, 이벤트 DeleteReply`() =
        runTest {
            // given
            val writerMe = mockk<User>(relaxed = true).apply { every { id } returns ME }
            val comment = mockk<Comment>(relaxed = true).apply { every { writer } returns writerMe }
            coEvery {
                commentRepository.getComment(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(comment)
            coEvery {
                replyRepository.getReplies(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(emptyList())
            coEvery {
                replyRepository.deleteReply(
                    DISCUSSION_ID,
                    COMMENT_ID,
                    1L,
                )
            } returns NetworkResult.Success(Unit)

            newVm(DISCUSSION_ID, COMMENT_ID)

            advanceUntilIdle()

            val ev = async { commentDetailViewModel.uiEvent.getOrAwaitValue() }

            // when
            commentDetailViewModel.deleteReply(1L)
            advanceUntilIdle()

            // then
            coVerify(exactly = 1) { replyRepository.deleteReply(DISCUSSION_ID, COMMENT_ID, 1L) }
            coVerify(exactly = 2) { replyRepository.getReplies(DISCUSSION_ID, COMMENT_ID) }
            assertEquals(CommentDetailUiEvent.DeleteReply, ev.await())
        }

    @Test
    fun `deleteComment 실패 - 에러 이벤트, isLoading false`() =
        runTest {
            // given
            val writerMe = mockk<User>(relaxed = true).apply { every { id } returns ME }
            val comment = mockk<Comment>(relaxed = true).apply { every { writer } returns writerMe }
            coEvery {
                commentRepository.getComment(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(comment)
            coEvery {
                replyRepository.getReplies(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(emptyList())
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery {
                commentRepository.deleteComment(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Failure(ex)

            newVm(DISCUSSION_ID, COMMENT_ID)

            advanceUntilIdle()

            val ev = async { commentDetailViewModel.uiEvent.getOrAwaitValue() }

            // when
            commentDetailViewModel.deleteComment()
            advanceUntilIdle()

            // then
            assertEquals(CommentDetailUiEvent.ShowError(ex), ev.await())
            assertFalse(commentDetailViewModel.uiState.getOrAwaitValue().isLoading)
        }

    @Test
    fun `reportComment 성공 - 성공 메시지 이벤트`() =
        runTest {
            // given
            val writerMe = mockk<User>(relaxed = true).apply { every { id } returns ME }
            val comment = mockk<Comment>(relaxed = true).apply { every { writer } returns writerMe }
            coEvery {
                commentRepository.getComment(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(comment)
            coEvery {
                replyRepository.getReplies(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(emptyList())
            coEvery {
                commentRepository.report(
                    DISCUSSION_ID,
                    COMMENT_ID,
                    any(),
                )
            } returns NetworkResult.Success(Unit)

            newVm(DISCUSSION_ID, COMMENT_ID)
            advanceUntilIdle()

            val ev = async { commentDetailViewModel.uiEvent.getOrAwaitValue() }

            // when
            commentDetailViewModel.reportComment("spam")
            advanceUntilIdle()

            // then
            assertEquals(CommentDetailUiEvent.ShowReportCommentSuccessMessage, ev.await())
        }

    @Test
    fun `showReplyCreate - 현재 content 포함해 이벤트 발행`() =
        runTest {
            // given
            val writerMe = mockk<User>(relaxed = true).apply { every { id } returns ME }
            val comment = mockk<Comment>(relaxed = true).apply { every { writer } returns writerMe }
            coEvery {
                commentRepository.getComment(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(comment)
            coEvery {
                replyRepository.getReplies(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Success(emptyList())

            newVm(DISCUSSION_ID, COMMENT_ID)
            advanceUntilIdle()

            commentDetailViewModel.updateContent("draft")
            val ev = async { commentDetailViewModel.uiEvent.getOrAwaitValue() }

            // when
            commentDetailViewModel.showReplyCreate()
            advanceUntilIdle()

            // then
            assertEquals(
                CommentDetailUiEvent.ShowReplyCreate(DISCUSSION_ID, COMMENT_ID, "draft"),
                ev.await(),
            )
        }

    companion object {
        private const val DISCUSSION_ID = 10L
        private const val COMMENT_ID = 99L
        private const val ME = 7L
        private const val OTHER = 13L
    }
}
