package com.team.todoktodok.presentation.vm

import androidx.lifecycle.SavedStateHandle
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.repository.ReplyRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.presentation.view.discussiondetail.replycreate.ReplyCreateUiEvent
import com.team.todoktodok.presentation.view.discussiondetail.replycreate.vm.ReplyCreateViewModel
import io.mockk.coEvery
import io.mockk.coVerify
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
class ReplyCreateViewModelTest {
    private lateinit var replyRepository: ReplyRepository
    private lateinit var replyCreateViewModel: ReplyCreateViewModel

    @BeforeEach
    fun setUp() {
        replyRepository = mockk(relaxed = true)
        replyCreateViewModel = newVmCreate()
    }

    private fun newVmCreate(
        content: String? = null,
        discussionId: Long = DISCUSSION_ID,
        commentId: Long = COMMENT_ID,
    ): ReplyCreateViewModel {
        val handle =
            SavedStateHandle(
                mapOf(
                    ReplyCreateViewModel.KEY_DISCUSSION_ID to discussionId,
                    ReplyCreateViewModel.KEY_COMMENT_ID to commentId,
                    ReplyCreateViewModel.KEY_REPLY_ID to null,
                    ReplyCreateViewModel.KEY_REPLY_CONTENT to content,
                ),
            )
        return ReplyCreateViewModel(handle, replyRepository)
    }

    private fun newVmUpdate(
        replyId: Long = REPLY_ID,
        content: String? = null,
        discussionId: Long = DISCUSSION_ID,
        commentId: Long = COMMENT_ID,
    ): ReplyCreateViewModel {
        val handle =
            SavedStateHandle(
                mapOf(
                    ReplyCreateViewModel.KEY_DISCUSSION_ID to discussionId,
                    ReplyCreateViewModel.KEY_COMMENT_ID to commentId,
                    ReplyCreateViewModel.KEY_REPLY_ID to replyId,
                    ReplyCreateViewModel.KEY_REPLY_CONTENT to content,
                ),
            )
        return ReplyCreateViewModel(handle, replyRepository)
    }

    @Test
    fun `Create - init은 content를 반영`() =
        runTest {
            replyCreateViewModel = newVmCreate(content = "이전 작성 내용")
            advanceUntilIdle()
            assertThat(replyCreateViewModel.replyContent.getOrAwaitValue()).isEqualTo("이전 작성 내용")
        }

    @Test
    fun `Update - init은 content를 반영`() =
        runTest {
            replyCreateViewModel = newVmUpdate(content = "업데이트 할 내용")
            advanceUntilIdle()
            assertThat(replyCreateViewModel.replyContent.getOrAwaitValue()).isEqualTo("업데이트 할 내용")
        }

    @Test
    fun `onReplyChanged는 replyContent 갱신`() =
        runTest {
            replyCreateViewModel = newVmCreate()
            replyCreateViewModel.onReplyChanged("지워질 내용")
            assertThat(replyCreateViewModel.replyContent.getOrAwaitValue()).isEqualTo("지워질 내용")
            replyCreateViewModel.onReplyChanged(null)
            assertThat(replyCreateViewModel.replyContent.getOrAwaitValue()).isEqualTo("")
        }

    @Test
    fun `Create - submitReply 성공 시 CreateReply 이벤트`() =
        runTest {
            replyCreateViewModel = newVmCreate()
            replyCreateViewModel.onReplyChanged("대댓글 생성 성공")
            coEvery { replyRepository.saveReply(DISCUSSION_ID, COMMENT_ID, "대댓글 생성 성공") } returns
                NetworkResult.Success(Unit)

            replyCreateViewModel.submitReply()
            advanceUntilIdle()
            val event = replyCreateViewModel.uiEvent.getOrAwaitValue()

            assertThat(event).isEqualTo(ReplyCreateUiEvent.CreateReply)
            coVerify(exactly = 1) {
                replyRepository.saveReply(
                    DISCUSSION_ID,
                    COMMENT_ID,
                    "대댓글 생성 성공",
                )
            }
        }

    @Test
    fun `Create - submitReply 실패 시 ShowErrorMessage`() =
        runTest {
            replyCreateViewModel = newVmCreate()
            replyCreateViewModel.onReplyChanged("대댓글 생성 실패")
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery { replyRepository.saveReply(DISCUSSION_ID, COMMENT_ID, "대댓글 생성 실패") } returns
                NetworkResult.Failure(ex)

            replyCreateViewModel.submitReply()
            advanceUntilIdle()
            val event = replyCreateViewModel.uiEvent.getOrAwaitValue()

            assertThat(event).isEqualTo(ReplyCreateUiEvent.ShowErrorMessage(ex))
        }

    @Test
    fun `Update - submitReply 성공 시 CreateReply 이벤트`() =
        runTest {
            replyCreateViewModel = newVmUpdate(replyId = REPLY_ID)
            replyCreateViewModel.onReplyChanged("대댓글 수정 성공")
            coEvery {
                replyRepository.updateReply(DISCUSSION_ID, COMMENT_ID, REPLY_ID, "대댓글 수정 성공")
            } returns NetworkResult.Success(Unit)

            replyCreateViewModel.submitReply()
            advanceUntilIdle()
            val event = replyCreateViewModel.uiEvent.getOrAwaitValue()

            assertThat(event).isEqualTo(ReplyCreateUiEvent.CreateReply)
            coVerify(exactly = 1) {
                replyRepository.updateReply(DISCUSSION_ID, COMMENT_ID, REPLY_ID, "대댓글 수정 성공")
            }
        }

    @Test
    fun `Update - submitReply 실패 시 ShowErrorMessage`() =
        runTest {
            replyCreateViewModel = newVmUpdate(replyId = REPLY_ID)
            replyCreateViewModel.onReplyChanged("대댓글 수정 실패")
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery {
                replyRepository.updateReply(DISCUSSION_ID, COMMENT_ID, REPLY_ID, "대댓글 수정 실패")
            } returns NetworkResult.Failure(ex)

            replyCreateViewModel.submitReply()
            advanceUntilIdle()
            val event = replyCreateViewModel.uiEvent.getOrAwaitValue()

            assertThat(event).isEqualTo(ReplyCreateUiEvent.ShowErrorMessage(ex))
        }

    @Test
    fun `Create - saveReply는 SaveContent 이벤트`() =
        runTest {
            val vm = newVmCreate()
            vm.onReplyChanged("작성중이던 대댓글 저장")

            vm.saveReply()
            advanceUntilIdle()
            val event = vm.uiEvent.getOrAwaitValue()

            assertThat(event).isEqualTo(ReplyCreateUiEvent.SaveContent("작성중이던 대댓글 저장"))
        }

    companion object {
        private const val DISCUSSION_ID = 10L
        private const val COMMENT_ID = 99L
        private const val REPLY_ID = 7L
    }
}
