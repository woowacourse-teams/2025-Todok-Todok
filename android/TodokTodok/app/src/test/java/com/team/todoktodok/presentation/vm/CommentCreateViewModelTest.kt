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
import kotlinx.coroutines.async
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
    private val DISCUSSION_ID = 10L
    private val COMMENT_ID = 99L
    private val INITIAL_TEXT = "prefilled"

    @BeforeEach
    fun setUp() {
        commentRepository = mockk(relaxed = true)
    }

    @Test
    fun `Create - initUiState는 commentContent 반영하고 InitState 이벤트를 보낸다`() =
        runTest {
            // given
            val handle =
                SavedStateHandle(
                    mapOf(
                        CommentCreateViewModel.KEY_DISCUSSION_ID to DISCUSSION_ID,
                        CommentCreateViewModel.KEY_COMMENT_CONTENT to INITIAL_TEXT,
                        CommentCreateViewModel.KEY_COMMENT_ID to null,
                    ),
                )
            val vm = CommentCreateViewModel(handle, commentRepository)

            // when
            vm.initUiState()
            advanceUntilIdle()

            // then
            assertThat(vm.commentText.getOrAwaitValue()).isEqualTo(INITIAL_TEXT)
            val ev = vm.uiEvent.getOrAwaitValue()
            assertThat(ev).isInstanceOf(CommentCreateUiEvent.InitState::class.java)
            assertThat((ev as CommentCreateUiEvent.InitState).content).isEqualTo(INITIAL_TEXT)
        }

    @Test
    fun `Update - initUiState 성공 시 서버 텍스트와 InitState 이벤트`() =
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
            val vm = CommentCreateViewModel(handle, commentRepository)
            val serverText = "from-server"
            val commentMock = mockk<Comment> { every { content } returns serverText }
            coEvery { commentRepository.getComment(DISCUSSION_ID, COMMENT_ID) } returns
                NetworkResult.Success(commentMock)

            // when
            vm.initUiState()
            advanceUntilIdle()

            // then
            assertThat(vm.commentText.getOrAwaitValue()).isEqualTo(serverText)
            val ev = vm.uiEvent.getOrAwaitValue()
            assertThat(ev).isInstanceOf(CommentCreateUiEvent.InitState::class.java)
            assertThat((ev as CommentCreateUiEvent.InitState).content).isEqualTo(serverText)
        }

    @Test
    fun `Update - initUiState 실패 시 ShowError`() =
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
            val vm = CommentCreateViewModel(handle, commentRepository)
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery { commentRepository.getComment(DISCUSSION_ID, COMMENT_ID) } returns
                NetworkResult.Failure(ex)

            // when
            val evDeferred = async { vm.uiEvent.getOrAwaitValue() }
            vm.initUiState()
            advanceUntilIdle()

            // then
            assertThat(evDeferred.await()).isEqualTo(CommentCreateUiEvent.ShowError(ex))
        }

    @Test
    fun `onCommentChanged는 commentText 갱신`() =
        runTest {
            // given
            val handle =
                SavedStateHandle(
                    mapOf(
                        CommentCreateViewModel.KEY_DISCUSSION_ID to DISCUSSION_ID,
                        CommentCreateViewModel.KEY_COMMENT_CONTENT to null,
                        CommentCreateViewModel.KEY_COMMENT_ID to null,
                    ),
                )
            val vm = CommentCreateViewModel(handle, commentRepository)

            // when + then
            vm.onCommentChanged("abc")
            assertThat(vm.commentText.getOrAwaitValue()).isEqualTo("abc")

            vm.onCommentChanged(null)
            assertThat(vm.commentText.getOrAwaitValue()).isEqualTo("")
        }

    @Test
    fun `Create - submitComment 성공 시 SubmitComment`() =
        runTest {
            // given
            val handle =
                SavedStateHandle(
                    mapOf(
                        CommentCreateViewModel.KEY_DISCUSSION_ID to DISCUSSION_ID,
                        CommentCreateViewModel.KEY_COMMENT_CONTENT to null,
                        CommentCreateViewModel.KEY_COMMENT_ID to null,
                    ),
                )
            val vm = CommentCreateViewModel(handle, commentRepository)
            vm.onCommentChanged("hello")
            coEvery { commentRepository.saveComment(DISCUSSION_ID, "hello") } returns
                NetworkResult.Success(Unit)

            // when
            val evDeferred = async { vm.uiEvent.getOrAwaitValue() }
            vm.submitComment()
            advanceUntilIdle()

            // then
            assertThat(evDeferred.await()).isEqualTo(CommentCreateUiEvent.SubmitComment)
            coVerify(exactly = 1) { commentRepository.saveComment(DISCUSSION_ID, "hello") }
        }

    @Test
    fun `Create - submitComment 실패 시 ShowError`() =
        runTest {
            // given
            val handle =
                SavedStateHandle(
                    mapOf(
                        CommentCreateViewModel.KEY_DISCUSSION_ID to DISCUSSION_ID,
                        CommentCreateViewModel.KEY_COMMENT_CONTENT to null,
                        CommentCreateViewModel.KEY_COMMENT_ID to null,
                    ),
                )
            val vm = CommentCreateViewModel(handle, commentRepository)
            vm.onCommentChanged("hello")
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery { commentRepository.saveComment(DISCUSSION_ID, "hello") } returns
                NetworkResult.Failure(ex)

            // when
            val evDeferred = async { vm.uiEvent.getOrAwaitValue() }
            vm.submitComment()
            advanceUntilIdle()

            // then
            assertThat(evDeferred.await()).isEqualTo(CommentCreateUiEvent.ShowError(ex))
        }

    @Test
    fun `Update - submitComment 성공 시 SubmitComment`() =
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
            val vm = CommentCreateViewModel(handle, commentRepository)
            vm.onCommentChanged("edited")
            coEvery { commentRepository.updateComment(DISCUSSION_ID, COMMENT_ID, "edited") } returns
                NetworkResult.Success(Unit)

            // when
            val evDeferred = async { vm.uiEvent.getOrAwaitValue() }
            vm.submitComment()
            advanceUntilIdle()

            // then
            assertThat(evDeferred.await()).isEqualTo(CommentCreateUiEvent.SubmitComment)
            coVerify(exactly = 1) {
                commentRepository.updateComment(DISCUSSION_ID, COMMENT_ID, "edited")
            }
        }

    @Test
    fun `Update - submitComment 실패 시 ShowError`() =
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
            val vm = CommentCreateViewModel(handle, commentRepository)
            vm.onCommentChanged("edited")
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery { commentRepository.updateComment(DISCUSSION_ID, COMMENT_ID, "edited") } returns
                NetworkResult.Failure(ex)

            // when
            val evDeferred = async { vm.uiEvent.getOrAwaitValue() }
            vm.submitComment()
            advanceUntilIdle()

            // then
            assertThat(evDeferred.await()).isEqualTo(CommentCreateUiEvent.ShowError(ex))
        }

    @Test
    fun `Create - saveContent는 OnCreateDismiss 이벤트`() =
        runTest {
            // given
            val handle =
                SavedStateHandle(
                    mapOf(
                        CommentCreateViewModel.KEY_DISCUSSION_ID to DISCUSSION_ID,
                        CommentCreateViewModel.KEY_COMMENT_CONTENT to null,
                        CommentCreateViewModel.KEY_COMMENT_ID to null,
                    ),
                )
            val vm = CommentCreateViewModel(handle, commentRepository)
            vm.onCommentChanged("temp")

            // when
            val evDeferred = async { vm.uiEvent.getOrAwaitValue() }
            vm.saveContent()
            advanceUntilIdle()

            // then
            assertThat(evDeferred.await())
                .isEqualTo(CommentCreateUiEvent.OnCreateDismiss("temp"))
        }

    companion object {
        private const val DISCUSSION_ID = 10L
        private const val COMMENT_ID = 99L
        private const val INITIAL_TEXT = "prefilled"
    }
}
