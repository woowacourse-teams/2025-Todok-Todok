package com.example.todoktodok.presentation.vm

import com.example.todoktodok.DISCUSSION_ROOMS
import com.example.todoktodok.InstantTaskExecutorExtension
import com.example.todoktodok.ext.getOrAwaitValue
import com.example.todoktodok.fake.FakeDiscussionRoomRepository
import com.example.todoktodok.presentation.view.discussion.DiscussionUiEvent
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
    fun `저장소에서 불러온 토론방 목록으로 값을 초기화 한다`() {
        //given
        val expected = DISCUSSION_ROOMS
        //then
        assertThat(discussionViewModel.discussionRooms.getOrAwaitValue()).isEqualTo(expected)
    }

    @Test
    fun `UiEvent를 NavigateAddDiscussionRoom으로 변경한다`() {
        //given
        val expected = DiscussionUiEvent.NavigateAddDiscussionRoom
        //when
        discussionViewModel.onUiEvent(DiscussionUiEvent.NavigateAddDiscussionRoom)
        //then
        assertThat(discussionViewModel.uiEvent.getOrAwaitValue()).isEqualTo(expected)
    }

    @Test
    fun `UiEvent를 NavigateDiscussionRoom으로 변경한다`() {
        //given
        val expected = DiscussionUiEvent.NavigateDiscussionRoom(1)
        //when
        discussionViewModel.onUiEvent(DiscussionUiEvent.NavigateDiscussionRoom(1))
        //then
        assertThat(discussionViewModel.uiEvent.getOrAwaitValue()).isEqualTo(expected)
    }
}