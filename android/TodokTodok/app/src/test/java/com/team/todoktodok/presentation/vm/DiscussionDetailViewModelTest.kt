package com.team.todoktodok.presentation.vm

import androidx.lifecycle.SavedStateHandle
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.fake.FakeDiscussionRepository
import com.team.todoktodok.fixture.COMMENTS
import com.team.todoktodok.fixture.DISCUSSIONS
import com.team.todoktodok.presentation.view.discussiondetail.vm.DiscussionDetailViewModel
import com.team.todoktodok.presentation.view.discussiondetail.vm.DiscussionDetailViewModel.Companion.KEY_DISCUSSION_ID
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
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        val state = SavedStateHandle(mapOf(KEY_DISCUSSION_ID to 2L))
        Dispatchers.setMain(testDispatcher)

        discussionRepository = FakeDiscussionRepository()
        discussionDetailViewModel =
            DiscussionDetailViewModel(
                state,
                discussionRepository,
            )
    }

    @Test
    fun `저장소에서 불러온 토론방을 가진다`() =
        runTest {
            // given
            val expected = DISCUSSIONS.find { it.id == 2L }
            // then
            assertThat(discussionDetailViewModel.discussion.getOrAwaitValue()).isEqualTo(
                expected,
            )
        }

    @Test
    fun `저장소에서 불러온 댓글들을 가진다`() =
        runTest {
            // given
            val expected = COMMENTS
            // then
            assertThat(discussionDetailViewModel.comments.getOrAwaitValue()).isEqualTo(
                expected,
            )
        }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
