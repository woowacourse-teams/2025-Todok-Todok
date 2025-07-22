package com.example.todoktodok.presentation.vm

import androidx.lifecycle.SavedStateHandle
import com.example.todoktodok.InstantTaskExecutorExtension
import com.example.todoktodok.ext.getOrAwaitValue
import com.example.todoktodok.fake.FakeCommentRepository
import com.example.todoktodok.fake.FakeDiscussionRoomRepository
import com.example.todoktodok.fixture.COMMENTS
import com.example.todoktodok.fixture.DISCUSSION_ROOMS
import com.example.todoktodok.presentation.view.discussion.detail.vm.DiscussionRoomDetailViewModel
import com.example.todoktodok.presentation.view.discussion.detail.vm.DiscussionRoomDetailViewModel.Companion.KEY_DISCUSSION_ID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class DiscussionRoomDetailViewModelTest {
    private lateinit var discussionRoomDetailViewModel: DiscussionRoomDetailViewModel

    @BeforeEach
    fun setUp() {
        val state = SavedStateHandle(mapOf(KEY_DISCUSSION_ID to 2L))

        discussionRoomDetailViewModel =
            DiscussionRoomDetailViewModel(
                state,
                FakeDiscussionRoomRepository(),
                FakeCommentRepository(),
            )
    }

    @Test
    fun `저장소에서 불러온 토론방을 가진다`() {
        // given
        val expected = DISCUSSION_ROOMS.find { it.id == 2L }
        // then
        assertThat(discussionRoomDetailViewModel.discussionRoom.getOrAwaitValue()).isEqualTo(
            expected,
        )
    }

    @Test
    fun `저장소에서 불러온 댓글들을 가진다`() {
        // given
        val expected = COMMENTS
        // then
        assertThat(discussionRoomDetailViewModel.comments.getOrAwaitValue()).isEqualTo(
            expected,
        )
    }
}
