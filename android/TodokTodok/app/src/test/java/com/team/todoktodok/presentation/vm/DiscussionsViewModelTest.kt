package com.team.todoktodok.presentation.vm

import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionFilter
import com.team.domain.model.exception.NetworkResult
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.fixture.DISCUSSIONS
import com.team.todoktodok.presentation.view.discussions.DiscussionsUiEvent
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class DiscussionsViewModelTest {
    private lateinit var mockDiscussionRepository: DiscussionRepository
    private lateinit var viewModel: DiscussionsViewModel

    @BeforeEach
    fun setUp() {
        mockDiscussionRepository = mockk(relaxed = true)
        viewModel = DiscussionsViewModel(mockDiscussionRepository)
    }

    @Test
    fun `새로운 탭으로 변경 시_uiState의 filter가 변경되고 loadDiscussions 호출된다`() =
        runTest {
            // Given
            val newTab = DiscussionFilter.MINE
            // TODO(API 완성시 수정)
            val tmpFilters = DiscussionFilter.entries.filterNot { it == DiscussionFilter.HOT }

            tmpFilters.forEach { filter ->
                coEvery {
                    mockDiscussionRepository.getDiscussions(filter, "")
                } returns NetworkResult.Success(emptyList())
            }

            // When
            viewModel.updateTab(newTab, 0L)

            // Then
            assertEquals(viewModel.uiState.value?.filter, newTab)
            tmpFilters.forEach { filter ->
                coVerify {
                    mockDiscussionRepository.getDiscussions(filter, "")
                }
            }
        }

    @Test
    fun `새로운 검색어로 변경 시_uiState의 searchKeyword가 변경되고 loadDiscussions이 호출된다`() =
        runTest {
            // Given
            val newKeyword = "나나짱~"

            val tmpFilters = DiscussionFilter.entries.filterNot { it == DiscussionFilter.HOT }

            tmpFilters
                .forEach { filter ->
                    coEvery {
                        mockDiscussionRepository.getDiscussions(filter, any())
                    } returns NetworkResult.Success(emptyList())
                }

            // When
            viewModel.loadSearchedDiscussions(newKeyword)

            // Then
            assertEquals(newKeyword, viewModel.uiState.value?.searchKeyword)

            tmpFilters.forEach { filter ->
                coVerify {
                    mockDiscussionRepository.getDiscussions(filter, newKeyword)
                }
            }
        }

    @Test
    fun `모든 토론 필터이고 토론 목록이_비어있으면 ShowNotHasAllDiscussions 이벤트가 발생한다,`() =
        runTest {
            // Given
            viewModel.updateTab(DiscussionFilter.ALL, 0L)
            DiscussionFilter.entries.forEach { filter ->
                coEvery {
                    mockDiscussionRepository.getDiscussions(filter, any())
                } returns NetworkResult.Success(emptyList())
            }

            // When
            viewModel.loadDiscussions()

            // Then
            assertThat(viewModel.uiState.value?.allDiscussions).isEmpty()
            assertEquals(
                viewModel.uiEvent.getOrAwaitValue(),
                DiscussionsUiEvent.ShowNotHasAllDiscussions,
            )
        }

    @Test
    fun `모든 토론 필터이고 토론 목록이 있을 때_ShowHasMyDiscussions 이벤트 발생하고 myDiscussions에 추가된다`() =
        runTest {
            // Given
            val mockDiscussions = DISCUSSIONS

            viewModel.updateTab(DiscussionFilter.ALL, 0L)

            DiscussionFilter.entries.forEach { filter ->
                coEvery {
                    mockDiscussionRepository.getDiscussions(filter, any())
                } returns NetworkResult.Success(DISCUSSIONS)
            }

            // When
            viewModel.loadDiscussions()

            // Then
            assertEquals(viewModel.uiState.value?.myDiscussions, mockDiscussions)
            assertEquals(
                viewModel.uiEvent.getOrAwaitValue(),
                DiscussionsUiEvent.ShowHasAllDiscussions,
            )
        }

    @Test
    fun `내 토론 필터이고 토론 목록이 있을 때_ShowHasMyDiscussions 이벤트가 발생하고 myDiscussions에 추가된다`() =
        runTest {
            // Given
            val mockDiscussions = DISCUSSIONS

            viewModel.updateTab(DiscussionFilter.MINE, 0L)

            DiscussionFilter.entries.forEach { filter ->
                coEvery {
                    mockDiscussionRepository.getDiscussions(filter, any())
                } returns NetworkResult.Success(DISCUSSIONS)
            }

            // When
            viewModel.loadDiscussions()

            // Then
            assertEquals(viewModel.uiState.value?.myDiscussions, mockDiscussions)
            assertEquals(
                viewModel.uiEvent.getOrAwaitValue(),
                DiscussionsUiEvent.ShowHasMyDiscussions,
            )
        }

    @Test
    fun `내 토론 필터이고 토론 목록이 없을 때 ShowNotHasMyDiscussions 이벤트 발생하고 myDiscussions에 추가된다`() =
        runTest {
            // Given
            viewModel.updateTab(DiscussionFilter.MINE, 0L)

            DiscussionFilter.entries.forEach { filter ->
                coEvery {
                    mockDiscussionRepository.getDiscussions(filter, any())
                } returns NetworkResult.Success(emptyList())
            }

            // When
            viewModel.loadDiscussions()

            // Then
            assertEquals(viewModel.uiState.value?.myDiscussions, emptyList<Discussion>())
            assertEquals(
                viewModel.uiEvent.getOrAwaitValue(),
                DiscussionsUiEvent.ShowNotHasMyDiscussions,
            )
        }
}
