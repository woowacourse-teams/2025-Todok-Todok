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
class ReplyCreateViewModelTest {
    private lateinit var replyRepository: ReplyRepository

    private val DISCUSSION_ID = 10L
    private val COMMENT_ID = 99L
    private val REPLY_ID = 7L

    @BeforeEach
    fun setUp() {
        replyRepository = mockk(relaxed = true)
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
            val vm = newVmCreate(content = "prefilled")
            advanceUntilIdle()
            assertThat(vm.replyContent.getOrAwaitValue()).isEqualTo("prefilled")
        }

    @Test
    fun `Update - init은 content를 반영`() =
        runTest {
            val vm = newVmUpdate(content = "from-server")
            advanceUntilIdle()
            assertThat(vm.replyContent.getOrAwaitValue()).isEqualTo("from-server")
        }

    @Test
    fun `onReplyChanged는 replyContent 갱신`() =
        runTest {
            val vm = newVmCreate()
            vm.onReplyChanged("abc")
            assertThat(vm.replyContent.getOrAwaitValue()).isEqualTo("abc")
            vm.onReplyChanged(null)
            assertThat(vm.replyContent.getOrAwaitValue()).isEqualTo("")
        }

    @Test
    fun `Create - submitReply 성공 시 CreateReply 이벤트`() =
        runTest {
            val vm = newVmCreate()
            vm.onReplyChanged("hello")
            coEvery { replyRepository.saveReply(DISCUSSION_ID, COMMENT_ID, "hello") } returns
                NetworkResult.Success(Unit)

            val evDeferred = async { vm.uiEvent.getOrAwaitValue() }
            vm.submitReply()
            advanceUntilIdle()

            assertThat(evDeferred.await()).isEqualTo(ReplyCreateUiEvent.CreateReply)
            coVerify(exactly = 1) { replyRepository.saveReply(DISCUSSION_ID, COMMENT_ID, "hello") }
        }

    @Test
    fun `Create - submitReply 실패 시 ShowErrorMessage`() =
        runTest {
            val vm = newVmCreate()
            vm.onReplyChanged("hello")
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery { replyRepository.saveReply(DISCUSSION_ID, COMMENT_ID, "hello") } returns
                NetworkResult.Failure(ex)

            val evDeferred = async { vm.uiEvent.getOrAwaitValue() }
            vm.submitReply()
            advanceUntilIdle()

            assertThat(evDeferred.await()).isEqualTo(ReplyCreateUiEvent.ShowErrorMessage(ex))
        }

    @Test
    fun `Update - submitReply 성공 시 CreateReply 이벤트`() =
        runTest {
            val vm = newVmUpdate(replyId = REPLY_ID)
            vm.onReplyChanged("edited")
            coEvery {
                replyRepository.updateReply(DISCUSSION_ID, COMMENT_ID, REPLY_ID, "edited")
            } returns NetworkResult.Success(Unit)

            val evDeferred = async { vm.uiEvent.getOrAwaitValue() }
            vm.submitReply()
            advanceUntilIdle()

            assertThat(evDeferred.await()).isEqualTo(ReplyCreateUiEvent.CreateReply)
            coVerify(exactly = 1) {
                replyRepository.updateReply(DISCUSSION_ID, COMMENT_ID, REPLY_ID, "edited")
            }
        }

    @Test
    fun `Update - submitReply 실패 시 ShowErrorMessage`() =
        runTest {
            val vm = newVmUpdate(replyId = REPLY_ID)
            vm.onReplyChanged("edited")
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery {
                replyRepository.updateReply(DISCUSSION_ID, COMMENT_ID, REPLY_ID, "edited")
            } returns NetworkResult.Failure(ex)

            val evDeferred = async { vm.uiEvent.getOrAwaitValue() }
            vm.submitReply()
            advanceUntilIdle()

            assertThat(evDeferred.await()).isEqualTo(ReplyCreateUiEvent.ShowErrorMessage(ex))
        }

    @Test
    fun `Create - saveReply는 SaveContent 이벤트`() =
        runTest {
            val vm = newVmCreate()
            vm.onReplyChanged("temp")

            val evDeferred = async { vm.uiEvent.getOrAwaitValue() }
            vm.saveReply()
            advanceUntilIdle()

            assertThat(evDeferred.await()).isEqualTo(ReplyCreateUiEvent.SaveContent("temp"))
        }
}
