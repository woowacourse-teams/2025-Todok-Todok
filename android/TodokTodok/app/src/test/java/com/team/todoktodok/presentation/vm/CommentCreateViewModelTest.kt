package com.team.todoktodok.presentation.vm

import androidx.lifecycle.SavedStateHandle
import com.team.domain.model.Comment
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.repository.CommentRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.presentation.view.discussiondetail.commentcreate.CommentCreateUiEvent
import com.team.todoktodok.presentation.view.discussiondetail.commentcreate.vm.CommentCreateViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantTaskExecutorExtension::class)
@ExtendWith(CoroutinesTestExtension::class)
class CommentCreateViewModelTest {
    private lateinit var commentRepository: CommentRepository
    private lateinit var commentCreateViewModel: CommentCreateViewModel

    private fun loadCommentCreateViewModelCreate(
        discussionId: Long = DISCUSSION_ID,
        initialText: String? = null,
    ) {
        val handle =
            SavedStateHandle(
                mapOf(
                    CommentCreateViewModel.KEY_DISCUSSION_ID to discussionId,
                    CommentCreateViewModel.KEY_COMMENT_ID to null, // Create 모드
                    CommentCreateViewModel.KEY_COMMENT_CONTENT to initialText,
                ),
            )
        commentCreateViewModel = CommentCreateViewModel(handle, commentRepository)
    }

    private fun loadCommentCreateViewModelUpdate(
        discussionId: Long = DISCUSSION_ID,
        commentId: Long = COMMENT_ID,
    ) {
        val handle =
            SavedStateHandle(
                mapOf(
                    CommentCreateViewModel.KEY_DISCUSSION_ID to discussionId,
                    CommentCreateViewModel.KEY_COMMENT_ID to commentId, // Update 모드
                    CommentCreateViewModel.KEY_COMMENT_CONTENT to null,
                ),
            )
        commentCreateViewModel = CommentCreateViewModel(handle, commentRepository)
    }

    @BeforeEach
    fun setUp() {
        commentRepository = mockk(relaxed = true)
        loadCommentCreateViewModelUpdate()
    }

    @Test
    fun `Create - initUiState는 commentContent 반영하고 InitState 이벤트를 보낸다`() =
        runTest {
            // given
            loadCommentCreateViewModelCreate()

            // when
            commentCreateViewModel.initUiState()
            advanceUntilIdle()

            val event = commentCreateViewModel.uiEvent.getOrAwaitValue()
            assertThat(event).isInstanceOf(CommentCreateUiEvent.InitState::class.java)
        }

    @Test
    fun `Update - initUiState 성공 시 최신 텍스트와 InitState 이벤트`() =
        runTest {
            // given
            val handle =
                SavedStateHandle(
                    mapOf(
                        CommentCreateViewModel.KEY_DISCUSSION_ID to DISCUSSION_ID,
                        CommentCreateViewModel.KEY_COMMENT_ID to COMMENT_ID,
                        CommentCreateViewModel.KEY_COMMENT_CONTENT to null,
                    ),
                )
            val commentCreateViewModel = CommentCreateViewModel(handle, commentRepository)
            val currentComment = "currentComment"
            val commentMock = mockk<Comment> { every { content } returns currentComment }
            coEvery { commentRepository.getComment(DISCUSSION_ID, COMMENT_ID) } returns
                NetworkResult.Success(commentMock)

            // when
            commentCreateViewModel.initUiState()
            advanceUntilIdle()

            // then
            assertThat(commentCreateViewModel.commentText.getOrAwaitValue()).isEqualTo(
                currentComment,
            )
            val event = commentCreateViewModel.uiEvent.getOrAwaitValue()
            assertThat(event).isInstanceOf(CommentCreateUiEvent.InitState::class.java)
            assertThat((event as CommentCreateUiEvent.InitState).content).isEqualTo(currentComment)
        }

    @Test
    fun `Update - initUiState 실패 시 ShowError`() =
        runTest {
            // given
            loadCommentCreateViewModelUpdate()
            val exception = TodokTodokExceptions.EmptyBodyException
            coEvery { commentRepository.getComment(DISCUSSION_ID, COMMENT_ID) } returns
                NetworkResult.Failure(exception)

            // when
            commentCreateViewModel.initUiState()
            advanceUntilIdle()
            val event = commentCreateViewModel.uiEvent.getOrAwaitValue()

            // then
            assertThat(event).isEqualTo(CommentCreateUiEvent.ShowError(exception))
        }

    @Test
    fun `onCommentChanged는 commentText 갱신`() =
        runTest {
            // when
            commentCreateViewModel.onCommentChanged("abc")
            // then
            assertThat(commentCreateViewModel.commentText.getOrAwaitValue()).isEqualTo("abc")
        }

    @Test
    fun `onCommentChanged는 commentText null로 갱신`() =
        runTest {
            // when
            commentCreateViewModel.onCommentChanged(null)
            // then
            assertThat(commentCreateViewModel.commentText.getOrAwaitValue()).isEqualTo("")
        }

    @Test
    fun `Create - submitComment 성공 시 SubmitComment`() =
        runTest {
            // given
            loadCommentCreateViewModelCreate()
            commentCreateViewModel.onCommentChanged("hello")
            coEvery { commentRepository.saveComment(DISCUSSION_ID, "hello") } returns
                NetworkResult.Success(Unit)

            // when
            commentCreateViewModel.submitComment()
            advanceUntilIdle()
            val event = commentCreateViewModel.uiEvent.getOrAwaitValue()

            // then
            assertThat(event).isEqualTo(CommentCreateUiEvent.SubmitComment)
            coVerify(exactly = 1) { commentRepository.saveComment(DISCUSSION_ID, "hello") }
        }

    @Test
    fun `Create - submitComment 실패 시 ShowError`() =
        runTest {
            // given
            loadCommentCreateViewModelCreate()
            commentCreateViewModel.onCommentChanged("hello")
            val exception = TodokTodokExceptions.EmptyBodyException
            coEvery { commentRepository.saveComment(DISCUSSION_ID, "hello") } returns
                NetworkResult.Failure(exception)

            // when
            commentCreateViewModel.submitComment()
            advanceUntilIdle()
            val event = commentCreateViewModel.uiEvent.getOrAwaitValue()

            // then
            assertThat(event).isEqualTo(CommentCreateUiEvent.ShowError(exception))
        }

    @Test
    fun `Update - submitComment 성공 시 SubmitComment`() =
        runTest {
            // given
            loadCommentCreateViewModelUpdate()
            commentCreateViewModel.onCommentChanged("edited")
            coEvery { commentRepository.updateComment(DISCUSSION_ID, COMMENT_ID, "edited") } returns
                NetworkResult.Success(Unit)

            // when
            commentCreateViewModel.submitComment()
            advanceUntilIdle()
            val event = commentCreateViewModel.uiEvent.getOrAwaitValue()

            // then
            assertThat(event).isEqualTo(CommentCreateUiEvent.SubmitComment)
            coVerify(exactly = 1) {
                commentRepository.updateComment(DISCUSSION_ID, COMMENT_ID, "edited")
            }
        }

    @Test
    fun `Update - submitComment 실패 시 ShowError`() =
        runTest {
            // given
            loadCommentCreateViewModelUpdate()
            commentCreateViewModel.onCommentChanged("edited")
            val exception = TodokTodokExceptions.EmptyBodyException
            coEvery { commentRepository.updateComment(DISCUSSION_ID, COMMENT_ID, "edited") } returns
                NetworkResult.Failure(exception)

            // when
            commentCreateViewModel.submitComment()
            advanceUntilIdle()
            val event = commentCreateViewModel.uiEvent.getOrAwaitValue()

            // then
            assertThat(event).isEqualTo(CommentCreateUiEvent.ShowError(exception))
        }

    @Test
    fun `Create - saveContent는 OnCreateDismiss 이벤트`() =
        runTest {
            // given
            loadCommentCreateViewModelCreate()
            commentCreateViewModel.onCommentChanged("temp")

            // when
            commentCreateViewModel.saveContent()
            advanceUntilIdle()
            val event = commentCreateViewModel.uiEvent.getOrAwaitValue()

            // then
            assertThat(event)
                .isEqualTo(CommentCreateUiEvent.OnCreateDismiss("temp"))
        }

    companion object {
        private const val DISCUSSION_ID = 10L
        private const val COMMENT_ID = 99L
    }
}
