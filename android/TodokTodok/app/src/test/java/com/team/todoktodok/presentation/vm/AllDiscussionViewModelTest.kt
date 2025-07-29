package com.team.todoktodok.presentation.vm

import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.fixture.DISCUSSIONS
import com.team.todoktodok.presentation.utview.discussions.all.AllDiscussionUiEvent
import com.team.todoktodok.presentation.utview.discussions.all.vm.AllDiscussionViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantTaskExecutorExtension::class)
@ExtendWith(CoroutinesTestExtension::class)
class AllDiscussionViewModelTest {
    private lateinit var repository: DiscussionRepository
    private lateinit var viewModel: AllDiscussionViewModel

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
    }

    @Test
    fun `초기화 시 토론 목록을 불러와 UiState에 저장한다`() =
        runTest {
            // given
            coEvery { repository.getDiscussions() } returns DISCUSSIONS

            // when
            viewModel = AllDiscussionViewModel(repository)

            // then
            val result = viewModel.uiState.getOrAwaitValue()
            assertEquals(DISCUSSIONS, result)
        }

    @Test
    fun `findDiscussion 호출 시 해당 Discussion ID로 이동 이벤트를 보낸다`() =
        runTest {
            // given
            coEvery { repository.getDiscussions() } returns DISCUSSIONS
            viewModel = AllDiscussionViewModel(repository)

            // when
            viewModel.findDiscussion(1)

            // then
            val event = viewModel.uiEvent.getOrAwaitValue()
            assertEquals(AllDiscussionUiEvent.NavigateToDetail(2L), event)
        }
}
