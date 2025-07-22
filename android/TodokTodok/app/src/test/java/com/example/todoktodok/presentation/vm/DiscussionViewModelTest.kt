package com.example.todoktodok.presentation.vm

import com.example.todoktodok.DISCUSSION_ROOMS
import com.example.todoktodok.InstantTaskExecutorExtension
import com.example.todoktodok.ext.getOrAwaitValue
import com.example.todoktodok.fake.FakeDiscussionRoomRepository
import com.example.todoktodok.presentation.view.discussion.vm.DiscussionViewModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class DiscussionViewModelTest {
    private lateinit var discussionViewModel: DiscussionViewModel

    @BeforeEach
    fun setUp() {
        discussionViewModel = DiscussionViewModel(FakeDiscussionRoomRepository())
    }

    @Test
    fun `토론방 초기화 테스트`() {
        // given
        val expected = DISCUSSION_ROOMS
        // then
        assertThat(discussionViewModel.discussionRooms.getOrAwaitValue()).isEqualTo(expected)
    }
}
