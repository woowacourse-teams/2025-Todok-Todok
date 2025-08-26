package com.team.todoktodok.presentation.vm

import androidx.lifecycle.SavedStateHandle
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.fake.FakeDiscussionRepository
import com.team.todoktodok.fixture.DISCUSSIONS
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailUiEvent
import com.team.todoktodok.presentation.view.discussiondetail.vm.DiscussionDetailViewModel
import com.team.todoktodok.presentation.view.discussiondetail.vm.DiscussionDetailViewModel.Companion.KEY_DISCUSSION_ID
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class DiscussionDetailViewModelTest {
    private lateinit var discussionDetailViewModel: DiscussionDetailViewModel
    private lateinit var discussionRepository: DiscussionRepository
    private lateinit var tokenRepository: TokenRepository
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        val state = SavedStateHandle(mapOf(KEY_DISCUSSION_ID to DISCUSSION_ID))
        Dispatchers.setMain(testDispatcher)

        tokenRepository = mockk(relaxed = true)
        discussionRepository = FakeDiscussionRepository()
        discussionDetailViewModel =
            DiscussionDetailViewModel(
                state,
                discussionRepository,
                tokenRepository,
            )
    }

    @Test
    fun `저장소에서 불러온 토론방을 가진다`() =
        runTest {
            // given
            val expected = DISCUSSIONS.find { it.id == DISCUSSION_ID }
            // then
            assertThat(
                discussionDetailViewModel.uiState
                    .getOrAwaitValue()
                    .discussion,
            ).isEqualTo(
                expected,
            )
        }

    @Test
    fun `토론방 수정 이벤트를 발생시킨다`() {
        // given
        val expected = DiscussionDetailUiEvent.UpdateDiscussion(DISCUSSION_ID)
        // when
        discussionDetailViewModel.updateDiscussion()
        // then
        assertThat(discussionDetailViewModel.uiEvent.getOrAwaitValue()).isEqualTo(expected)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    companion object {
        private const val DISCUSSION_ID = 2L
    }
}
