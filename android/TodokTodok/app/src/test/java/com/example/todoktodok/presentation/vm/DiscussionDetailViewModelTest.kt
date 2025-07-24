package com.example.todoktodok.presentation.vm

import androidx.lifecycle.SavedStateHandle
import com.example.domain.repository.CommentRepository
import com.example.domain.repository.DiscussionRepository
import com.example.todoktodok.InstantTaskExecutorExtension
import com.example.todoktodok.ext.getOrAwaitValue
import com.example.todoktodok.fake.FakeCommentRepository
import com.example.todoktodok.fake.FakeDiscussionRepository
import com.example.todoktodok.fixture.COMMENTS
import com.example.todoktodok.fixture.DISCUSSIONS
import com.example.todoktodok.presentation.view.discussion.detail.vm.DiscussionDetailViewModel
import com.example.todoktodok.presentation.view.discussion.detail.vm.DiscussionDetailViewModel.Companion.KEY_DISCUSSION_ID
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
    private lateinit var commentRepository: CommentRepository
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        val state = SavedStateHandle(mapOf(KEY_DISCUSSION_ID to 2L))
        Dispatchers.setMain(testDispatcher)

        discussionRepository = FakeDiscussionRepository()
        commentRepository = FakeCommentRepository()
        discussionDetailViewModel =
            DiscussionDetailViewModel(
                state,
                discussionRepository,
                commentRepository,
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
