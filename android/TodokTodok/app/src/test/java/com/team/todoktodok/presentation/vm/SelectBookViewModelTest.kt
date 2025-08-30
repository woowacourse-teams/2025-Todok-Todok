package com.team.todoktodok.presentation.vm

import com.team.domain.model.book.AladinBook.Companion.AladinBook
import com.team.domain.model.book.AladinBooks
import com.team.domain.model.exception.NetworkResult
import com.team.domain.repository.BookRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.presentation.view.book.SearchedBookResultStatus
import com.team.todoktodok.presentation.view.book.SelectBookUiEvent
import com.team.todoktodok.presentation.view.book.vm.SelectBookViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class SelectBookViewModelTest {
    private lateinit var bookRepository: BookRepository
    private lateinit var viewModel: SelectBookViewModel

    @BeforeEach
    fun setUp() {
        bookRepository = mockk()
        viewModel = SelectBookViewModel(bookRepository)
    }

    @Test
    fun `키워드 입력시 SelectBookUiState의 keyword가 업데이트 된다`() =
        runTest {
            // given
            val keyword = "오브젝트"

            // when
            viewModel.updateKeyword(keyword)

            // then
            val actual = viewModel.uiState.getOrAwaitValue()
            assertEquals(
                keyword,
                actual.keyword,
            )
        }

    @Test
    fun `빈 키워드 입력시 도서 리스트를 찾지 않는다`() =
        runTest {
            // given
            val keyword = ""

            // when
            viewModel.searchWithCurrentKeyword(keyword)

            // then
            val actual = viewModel.uiState.getOrAwaitValue()
            assertEquals(
                SearchedBookResultStatus.NotStarted,
                actual.status,
            )
        }

    @Test
    fun `공백 키워드 입력시 도서 리스트를 찾지 않는다`() =
        runTest {
            // given
            val keyword = "    "

            // when
            viewModel.searchWithCurrentKeyword(keyword)

            // then
            val actual = viewModel.uiState.getOrAwaitValue()
            assertEquals(
                SearchedBookResultStatus.NotStarted,
                actual.status,
            )
        }

    @Test
    fun `전과 동일한 키워드 입력시 도서 리스트를 찾지 않는다`() =
        runTest {
            // given
            viewModel.updateKeyword("오브젝트")
            val keyword = "오브젝트"

            // when
            viewModel.searchWithCurrentKeyword(keyword)

            // then
            val actual = viewModel.uiState.getOrAwaitValue()
            assertEquals(
                SearchedBookResultStatus.NotStarted,
                actual.status,
            )
        }

    @Test
    fun `정상적인 키워드 입력시 로딩 후 책 목록이 저장된다`() =
        runTest {
            // given
            val keyword = "오브젝트"
            val books =
                AladinBooks(
                    listOf(
                        AladinBook(
                            isbn = 9791158391409L,
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
        }

    @Test
    fun `유효한 포지션의 책을 선택하면, NavigateToCreateDiscussionRoom 이벤트가 발생한다`() =
        runTest {
            // given
            val keyword = "오브젝트"
            val book =
                AladinBook(
                    isbn = 9791158391409L,
                    title = "오브젝트",
                    author = "조영호",
                    image = "https://image.aladin.co.kr/product/19368/10/cover200/k972635015_1.jpg",
                )
            val books =
                AladinBooks(
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
