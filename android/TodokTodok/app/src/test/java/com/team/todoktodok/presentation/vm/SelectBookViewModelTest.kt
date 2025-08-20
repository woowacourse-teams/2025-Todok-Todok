package com.team.todoktodok.presentation.vm

import com.team.domain.model.Book
import com.team.domain.model.Books
import com.team.domain.model.exception.NetworkResult
import com.team.domain.repository.BookRepository
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.presentation.view.book.SelectBookErrorType
import com.team.todoktodok.presentation.view.book.SelectBookUiEvent
import com.team.todoktodok.presentation.view.book.vm.SelectBookViewModel
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

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class SelectBookViewModelTest {
    private lateinit var bookRepository: BookRepository
    private lateinit var discussionRepository: DiscussionRepository
    private lateinit var viewModel: SelectBookViewModel

    @BeforeEach
    fun setUp() {
        bookRepository = mockk()
        discussionRepository = mockk<DiscussionRepository>(relaxed = true)
        viewModel = SelectBookViewModel(bookRepository, discussionRepository)
    }

    @Test
    fun `빈 키워드 입력시 ERROR_EMPTY_KEYWORD 이벤트가 발생한다`() =
        runTest {
            // given
            val keyword = ""
            coVerify { discussionRepository.hasDiscussion() }

            // when
            viewModel.searchWithCurrentKeyword(keyword)

            // then
            val actual = viewModel.uiEvent.getOrAwaitValue()
            assertEquals(
                SelectBookUiEvent.ShowErrorMessage(SelectBookErrorType.ERROR_EMPTY_KEYWORD),
                actual,
            )
        }

    @Test
    fun `전과 동일한 키워드 입력시 ERROR_SAME_KEYWORD 이벤트가 발생한다`() =
        runTest {
            // given
            viewModel.updateKeyword("오브젝트")
            val keyword = "오브젝트"
            coVerify { discussionRepository.hasDiscussion() }

            // when
            viewModel.searchWithCurrentKeyword(keyword)

            // then
            val actual = viewModel.uiEvent.getOrAwaitValue()
            assertEquals(
                SelectBookUiEvent.ShowErrorMessage(SelectBookErrorType.ERROR_SAME_KEYWORD),
                actual,
            )
        }

    @Test
    fun `정상적인 키워드 입력시 로딩 후 책 목록이 저장된다`() =
        runTest {
            // given
            val keyword = "오브젝트"
            val books =
                Books(
                    listOf(
                        Book(
                            id = 5L,
                            title = "오브젝트",
                            author = "조영호",
                            image = "https://image.aladin.co.kr/product/19368/10/cover200/k972635015_1.jpg",
                        ),
                    ),
                )
            coEvery { bookRepository.fetchBooks(keyword) } returns NetworkResult.Success(books)

            // when
            viewModel.searchWithCurrentKeyword(keyword)

            // then
            val actual = viewModel.uiState.getOrAwaitValue()
            assertEquals(books, actual.searchedBooks)
            assertThat(actual.isLoading).isFalse()
        }

    @Test
    fun `유효하지 않는 포지션의 책을 선택하면, ERROR_NO_SELECTED_BOOK 이벤트가 발생한다`() =
        runTest {
            // given
            val position = -1
            coVerify { discussionRepository.hasDiscussion() }

            // when
            viewModel.updateSelectedBook(position)

            // then
            val event = viewModel.uiEvent.getOrAwaitValue()
            assertEquals(
                SelectBookUiEvent.ShowErrorMessage(SelectBookErrorType.ERROR_NO_SELECTED_BOOK),
                event,
            )
        }

    @Test
    fun `유효한 포지션의 책을 선택하면, NavigateToCreateDiscussionRoom 이벤트가 발생한다`() =
        runTest {
            // given
            val keyword = "오브젝트"
            val book =
                Book(
                    id = 5L,
                    title = "오브젝트",
                    author = "조영호",
                    image = "https://image.aladin.co.kr/product/19368/10/cover200/k972635015_1.jpg",
                )
            val books =
                Books(
                    listOf(
                        book,
                    ),
                )
            coEvery { bookRepository.fetchBooks(keyword) } returns NetworkResult.Success(books)

            // when
            viewModel.searchWithCurrentKeyword(keyword)
            viewModel.updateSelectedBook(0)

            // then
            val event = viewModel.uiEvent.getOrAwaitValue()
            assertEquals(
                SelectBookUiEvent.NavigateToCreateDiscussionRoom(book),
                event,
            )
        }
}
