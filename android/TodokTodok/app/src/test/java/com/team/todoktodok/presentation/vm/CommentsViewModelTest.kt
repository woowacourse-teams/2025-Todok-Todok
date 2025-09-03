package com.team.todoktodok.presentation.vm

import androidx.lifecycle.SavedStateHandle
import com.team.domain.model.LikeStatus
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.fixture.COMMENTS
import com.team.todoktodok.presentation.view.discussiondetail.comments.CommentsUiEvent
import com.team.todoktodok.presentation.view.discussiondetail.comments.vm.CommentsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class CommentsViewModelTest {
    private lateinit var vm: CommentsViewModel
    private lateinit var repo: CommentRepository
    private lateinit var tokenRepo: TokenRepository

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repo = mockk(relaxed = true)
        tokenRepo = mockk(relaxed = true)

        coEvery { tokenRepo.getMemberId() } returns ME
        coEvery { repo.getCommentsByDiscussionId(DISCUSSION_ID) } returns
            NetworkResult.Success(COMMENTS)

        val state = SavedStateHandle(mapOf(CommentsViewModel.KEY_DISCUSSION_ID to DISCUSSION_ID))
        vm = CommentsViewModel(state, repo, tokenRepo)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `초기 로드 성공시 comments 채워지고 isLoading=false`() =
        runTest {
            val ui = vm.uiState.getOrAwaitValue()
            advanceUntilIdle()
            assertThat(ui.isLoading).isFalse()
            assertThat(ui.comments.map { it.comment.id }).containsExactly(1L, 2L, 3L, 4L, 5L)
        }

    @Test
    fun `showNewComment는 목록 재로딩 후 이벤트 발생`() =
        runTest {
            vm.showNewComment()
            val observed = async { vm.uiEvent.getOrAwaitValue() }
            assertThat(observed.await()).isEqualTo(CommentsUiEvent.ShowNewComment)
        }

    @Test
    fun `toggleLike 성공 시 해당 코멘트만 재로딩되어 갱신`() =
        runTest {
            // 초기 로드
            advanceUntilIdle()

            coEvery { repo.toggleLike(DISCUSSION_ID, 1L) } returns
                NetworkResult.Success(
                    LikeStatus.LIKE,
                )
            val c1Updated = COMMENTS[0].copy(isLikedByMe = true, likeCount = 1)
            coEvery { repo.getComment(DISCUSSION_ID, 1L) } returns NetworkResult.Success(c1Updated)

            vm.toggleLike(1L)
            advanceUntilIdle()

            val ui = vm.uiState.getOrAwaitValue()
            val updated = ui.comments.first { it.comment.id == 1L }.comment
            assertThat(updated.isLikedByMe).isTrue()
            assertThat(updated.likeCount).isEqualTo(1)
            val untouched = ui.comments.first { it.comment.id == 2L }.comment
            assertThat(untouched.isLikedByMe).isFalse()
        }

    @Test
    fun `deleteComment 성공 시 목록 재로딩되고 DeleteComment 이벤트 발생`() =
        runTest {
            coEvery { repo.deleteComment(DISCUSSION_ID, 2L) } returns NetworkResult.Success(Unit)
            coEvery { repo.getCommentsByDiscussionId(DISCUSSION_ID) } returns
                NetworkResult.Success(
                    COMMENTS.minus(COMMENTS[1]),
                )

            vm.deleteComment(2L)
            advanceUntilIdle()
            val observed = async { vm.uiEvent.getOrAwaitValue() }
            val ui = vm.uiState.getOrAwaitValue()
            assertThat(observed.await()).isEqualTo(CommentsUiEvent.DeleteComment)
            assertThat(ui.comments.map { it.comment.id }).containsExactly(1L, 3L, 4L, 5L)
        }

    @Test
    fun `updateComment는 ShowCommentUpdate 이벤트 발생`() =
        runTest {
            val expected = CommentsUiEvent.ShowCommentUpdate(DISCUSSION_ID, 1L, "edit")
            vm.updateComment(commentId = 1L, content = "edit")
            val observed = async { vm.uiEvent.getOrAwaitValue() }

            assertThat(observed.await()).isEqualTo(expected)
        }

    @Test
    fun `report 성공 시 성공 메시지 이벤트`() =
        runTest {
            coEvery { repo.report(DISCUSSION_ID, 2L, any()) } returns NetworkResult.Success(Unit)

            vm.reportComment(2L, "스팸")
            val observed = async { vm.uiEvent.getOrAwaitValue() }
            advanceUntilIdle()

            assertThat(observed.await()).isEqualTo(CommentsUiEvent.ShowReportCommentSuccessMessage)
        }

    @Test
    fun `report 실패 시 에러 이벤트와 isLoading=false`() =
        runTest {
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery { repo.report(DISCUSSION_ID, 2L, any()) } returns NetworkResult.Failure(ex)

            vm.reportComment(2L, "사유")
            val observed = async { vm.uiEvent.getOrAwaitValue() }

            assertThat(observed.await()).isEqualTo(CommentsUiEvent.ShowError(ex))
            assertThat(vm.uiState.getOrAwaitValue().isLoading).isFalse()
        }

    @Test
    fun `showCommentCreate는 현재 입력값과 함께 이벤트 발생`() =
        runTest {
            // 입력값 업데이트
            vm.updateCommentContent("hello")
            vm.showCommentCreate()
            val observed = async { vm.uiEvent.getOrAwaitValue() }
            advanceUntilIdle()

            assertThat(observed.await()).isEqualTo(
                CommentsUiEvent.ShowCommentCreate(discussionId = DISCUSSION_ID, content = "hello"),
            )
        }

    @Test
    fun `updateCommentContent는 uiState commentContent를 갱신`() =
        runTest {
            vm.updateCommentContent("abc")
            val ui = vm.uiState.getOrAwaitValue()
            assertThat(ui.commentContent).isEqualTo("abc")
        }

    @Test
    fun `초기 로드 실패 시 에러 이벤트와 isLoading=false`() =
        runTest {
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery { repo.getCommentsByDiscussionId(DISCUSSION_ID) } returns
                NetworkResult.Failure(
                    ex,
                )

            val state =
                SavedStateHandle(mapOf(CommentsViewModel.KEY_DISCUSSION_ID to DISCUSSION_ID))
            val vm2 = CommentsViewModel(state, repo, tokenRepo)

            val observed = async { vm2.uiEvent.getOrAwaitValue() }
            advanceUntilIdle()

            assertThat(observed.await()).isEqualTo(CommentsUiEvent.ShowError(ex))
            assertThat(vm2.uiState.getOrAwaitValue().isLoading).isFalse()
        }

    companion object {
        private const val DISCUSSION_ID = 10L
        private const val ME = 100L
    }
}
