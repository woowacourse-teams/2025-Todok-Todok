package com.example.todoktodok.presentation.vm

import com.example.todoktodok.InstantTaskExecutorExtension
import com.example.todoktodok.ext.getOrAwaitValue
import com.example.todoktodok.fake.FakeDiscussionRepository
import com.example.todoktodok.fixture.DISCUSSIONS
import com.example.todoktodok.presentation.view.discussion.discussions.DiscussionUiEvent
import com.example.todoktodok.presentation.view.discussion.discussions.vm.DiscussionViewModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class DiscussionViewModelTest {
    private lateinit var discussionViewModel: DiscussionViewModel

    @BeforeEach
    fun setUp() {
        discussionViewModel = DiscussionViewModel(FakeDiscussionRepository())
    }

    @Test
    fun `저장소에서 불러온 토론방 목록으로 값을 초기화 한다`() {
        // given
        val expected = DISCUSSIONS
        // then
        assertThat(discussionViewModel.discussions.getOrAwaitValue()).isEqualTo(expected)
    }

    @Test
    fun `UiEvent를 NavigateAddDiscussion으로 변경한다`() {
        // given
        val expected = DiscussionUiEvent.NavigateToAddDiscussion
        // when
        discussionViewModel.onUiEvent(DiscussionUiEvent.NavigateToAddDiscussion)
        // then
        assertThat(discussionViewModel.uiEvent.getOrAwaitValue()).isEqualTo(expected)
    }

    @Test
    fun `UiEvent를 NavigateDiscussion으로 변경한다`() {
        // given
        val expected = DiscussionUiEvent.NavigateToDiscussionDetail(1)
        // when
        discussionViewModel.onUiEvent(DiscussionUiEvent.NavigateToDiscussionDetail(1))
        // then
        assertThat(discussionViewModel.uiEvent.getOrAwaitValue()).isEqualTo(expected)
    }
}
