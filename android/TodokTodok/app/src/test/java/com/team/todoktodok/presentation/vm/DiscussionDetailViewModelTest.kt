package com.team.todoktodok.presentation.vm

import androidx.lifecycle.SavedStateHandle
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.fixture.DISCUSSIONS
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailUiEvent
import com.team.todoktodok.presentation.xml.discussiondetail.vm.DiscussionDetailViewModel
import com.team.todoktodok.presentation.xml.discussiondetail.vm.DiscussionDetailViewModel.Companion.KEY_DISCUSSION_ID
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
@ExtendWith(CoroutinesTestExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class DiscussionDetailViewModelTest {
    private lateinit var discussionDetailViewModel: DiscussionDetailViewModel
    private lateinit var discussionRepository: DiscussionRepository
    private lateinit var tokenRepository: TokenRepository

    @BeforeEach
    fun setUp() {
        val state = SavedStateHandle(mapOf(KEY_DISCUSSION_ID to DISCUSSION_ID))
        tokenRepository = mockk(relaxed = true)
        coEvery { tokenRepository.getMemberId() } returns WRITER_ID
        discussionRepository = mockk(relaxed = true)
        coEvery { discussionRepository.getDiscussion(DISCUSSION_ID) } returns
            NetworkResult.Success(DISCUSSIONS.first())

        discussionDetailViewModel =
            DiscussionDetailViewModel(
                state,
                discussionRepository,
                tokenRepository,
            )

        discussionDetailViewModel.initLoadDiscission(DISCUSSION_ID)
    }

    @Test
    fun `저장소에서 불러온 토론방을 가진다`() =
        runTest {
            // given
            val expected =
                (discussionRepository.getDiscussion(DISCUSSION_ID) as NetworkResult.Success).data

            // when
            advanceUntilIdle()

            // then
            assertThat(
                discussionDetailViewModel.uiState.getOrAwaitValue().discussion,
            ).isEqualTo(expected)
        }

    @Test
    fun `토론방 수정 이벤트를 발생시킨다`() =
        runTest {
            // given
            val expected = DiscussionDetailUiEvent.UpdateDiscussion(DISCUSSION_ID)

            // when
            discussionDetailViewModel.updateDiscussion()
            advanceUntilIdle()
            val event = discussionDetailViewModel.uiEvent.getOrAwaitValue()

            // then
            assertThat(event).isEqualTo(expected)
        }

    @Test
    fun `댓글 보기 이벤트를 발생시킨다`() =
        runTest {
            // given
            val expected = DiscussionDetailUiEvent.ShowComments(DISCUSSION_ID)

            // when
            discussionDetailViewModel.showComments()
            advanceUntilIdle()
            val event = discussionDetailViewModel.uiEvent.getOrAwaitValue()

            // then
            assertThat(event).isEqualTo(expected)
        }

    @Test
    fun `프로필 이동 이벤트를 발생시킨다`() =
        runTest {
            // given
            val expected = DiscussionDetailUiEvent.NavigateToProfile(WRITER_ID)

            // when
            discussionDetailViewModel.navigateToProfile()
            advanceUntilIdle()
            val event = discussionDetailViewModel.uiEvent.getOrAwaitValue()

            // then
            assertThat(event).isEqualTo(expected)
        }

    @Test
    fun `토론방 신고 성공 시 성공 메시지 이벤트를 발생시킨다`() =
        runTest {
            // given
            coEvery { discussionRepository.reportDiscussion(DISCUSSION_ID, any()) } returns
                NetworkResult.Success(Unit)

            // when
            discussionDetailViewModel.reportDiscussion("스팸")
            advanceUntilIdle()
            val event = discussionDetailViewModel.uiEvent.getOrAwaitValue()

            // then
            assertThat(event).isEqualTo(DiscussionDetailUiEvent.ShowReportDiscussionSuccessMessage)
        }

    @Test
    fun `토론방 신고 실패 시 에러 이벤트와 isLoading false로 내린다`() =
        runTest {
            // given
            val exception = TodokTodokExceptions.EmptyBodyException
            coEvery { discussionRepository.reportDiscussion(DISCUSSION_ID, any()) } returns
                NetworkResult.Failure(exception)

            // when
            discussionDetailViewModel.reportDiscussion("사유")
            advanceUntilIdle()
            val event = discussionDetailViewModel.uiEvent.getOrAwaitValue()
            val uiState = discussionDetailViewModel.uiState.getOrAwaitValue()

            // then
            assertThat(event).isEqualTo(DiscussionDetailUiEvent.ShowErrorMessage(exception))
            assertFalse(uiState.isLoading)
        }

    @Test
    fun `토론방 삭제 성공 시 삭제 이벤트를 발생시킨다`() =
        runTest {
            // given
            coEvery { discussionRepository.deleteDiscussion(DISCUSSION_ID) } returns
                NetworkResult.Success(Unit)

            // when
            discussionDetailViewModel.deleteDiscussion()
            advanceUntilIdle()
            val event = discussionDetailViewModel.uiEvent.getOrAwaitValue()

            // then
            assertThat(event).isEqualTo(DiscussionDetailUiEvent.DeleteDiscussion(DISCUSSION_ID))
        }

    @Test
    fun `토론방 삭제 실패 시 에러 이벤트와 isLoading false로 내린다`() =
        runTest {
            // given
            val exception = TodokTodokExceptions.EmptyBodyException
            coEvery { discussionRepository.deleteDiscussion(DISCUSSION_ID) } returns
                NetworkResult.Failure(exception)

            // when
            discussionDetailViewModel.deleteDiscussion()
            advanceUntilIdle()
            val event = discussionDetailViewModel.uiEvent.getOrAwaitValue()
            val uiState = discussionDetailViewModel.uiState.getOrAwaitValue()

            // then
            assertThat(event).isEqualTo(DiscussionDetailUiEvent.ShowErrorMessage(exception))
            assertFalse(uiState.isLoading)
        }

    companion object {
        private const val DISCUSSION_ID = 2L
        private const val WRITER_ID = 1L
    }
}
