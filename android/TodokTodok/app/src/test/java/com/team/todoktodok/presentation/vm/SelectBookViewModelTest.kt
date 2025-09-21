package com.team.todoktodok.presentation.vm

import com.team.domain.model.book.AladinBook.Companion.AladinBook
import com.team.domain.model.book.AladinBooks
import com.team.domain.model.book.Keyword
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.repository.BookRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.fixture.AladinBookFixtures.books
import com.team.todoktodok.presentation.xml.book.SearchedBookStatus
import com.team.todoktodok.presentation.xml.book.SelectBookUiEvent
import com.team.todoktodok.presentation.xml.book.vm.SelectBookViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
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
        coEvery { bookRepository.fetchBooks(any()) } returns NetworkResult.Success(AladinBooks(emptyList()))
    }

    @Test
    fun `키워드 입력시 SelectBookUiState의 keyword가 업데이트 된다`() {
        // given
        val keyword = "오브젝트"

        // when
        viewModel.searchWithCurrentKeyword(keyword)

        // then
        val actual = viewModel.uiState.getOrAwaitValue()
        assertEquals(
            Keyword(keyword),
            actual.keyword,
        )
    }

    @Test
    fun `빈 키워드 입력시 도서 리스트를 찾지 않는다`() {
        // given
        val keyword = ""

        // when
        viewModel.searchWithCurrentKeyword(keyword)

        // then
        val actual = viewModel.uiState.getOrAwaitValue()
        assertEquals(
            SearchedBookStatus.NotStarted,
            actual.status,
        )
    }

    @Test
    fun `공백 키워드 입력시 도서 리스트를 찾지 않는다`() {
        // given
        val keyword = "    "

        // when
        viewModel.searchWithCurrentKeyword(keyword)

        // then
        val actual = viewModel.uiState.getOrAwaitValue()
        assertEquals(
            SearchedBookStatus.NotStarted,
            actual.status,
        )
    }

    @Test
    fun `전과 동일한 키워드 입력시 도서 리스트를 찾지 않는다`() =
        runTest {
            // given
            val keyword = "오브젝트"
            viewModel.updateKeyword(keyword)
            coEvery { bookRepository.fetchBooks(Keyword(keyword)) } returns
                NetworkResult.Success(
                    books,
                )

            // when
            viewModel.searchWithCurrentKeyword(keyword)
            advanceUntilIdle()
            // then
            val actual = viewModel.uiState.getOrAwaitValue()
            assertEquals(
                SearchedBookStatus.NotStarted,
                actual.status,
            )
        }

    @Test
    fun `유효한 포지션의 책을 선택하면, NavigateToCreateDiscussionRoom 이벤트가 발생한다`() =
        runTest {
            // given
            val keyword = "엘레강트 오브젝트"
            val book =
                AladinBook(
                    9791187497219L,
                    "엘레강트 오브젝트 - 새로운 관점에서 바라본 객체지향",
                    "Yegor Bugayenko (지은이), 조영호 (옮긴이)",
                    "https://image.aladin.co.kr/product/25837/40/coversum/k762736538_1.jpg",
                )
            val books =
                AladinBooks(
                    listOf(
                        book,
                    ),
                )
            coEvery { bookRepository.fetchBooks(Keyword(keyword)) } returns
                NetworkResult.Success(
                    books,
                )

            // when
            viewModel.searchWithCurrentKeyword(keyword)
            viewModel.updateSelectedBook(0)

            // then
            val actual = viewModel.uiEvent.getOrAwaitValue()
            assertEquals(
                SelectBookUiEvent.NavigateToCreateDiscussionRoom(book),
                actual,
            )
        }

    @Test
    fun `도서를 불러오다가 실패하면, 에러가 나고 화면은 시작하지 않는 상태로 전환된다`() =
        runTest {
            // given
            val keyword = "kotlin in action"
            coEvery {
                viewModel.searchWithCurrentKeyword(keyword)
                bookRepository.fetchBooks(Keyword(keyword))
            } returns
                NetworkResult.Failure(TodokTodokExceptions.EmptyBodyException)

            // when
            viewModel.searchWithCurrentKeyword(keyword)

            // then
            val actualUiState = viewModel.uiState.getOrAwaitValue()
            val actualUiEvent = viewModel.uiEvent.getOrAwaitValue()

            assertAll(
                { assertEquals(actualUiState.status, SearchedBookStatus.NotStarted) },
                { assertInstanceOf(SelectBookUiEvent.ShowException::class.java, actualUiEvent) },
            )
        }

    @Test
    fun `도서를 불러왔을 때, 빈 리스트이면, 책을 찾을 수 없는 상태로 전환된다`() =
        runTest {
            // given
            val keyword = "객체 지향의 사실과 오해"
            coEvery { bookRepository.fetchBooks(Keyword(keyword)) } returns
                NetworkResult.Success(
                    AladinBooks(
                        emptyList(),
                    ),
                )

            // when
            viewModel.searchWithCurrentKeyword(keyword)

            // then
            val actual = viewModel.uiState.getOrAwaitValue().status
            val expected = SearchedBookStatus.NotFound

            assertEquals(expected, actual)
        }

    @Test
    fun `도서를 불러왔을 때, 값이 있으면, 도서를 찾은 상태로 전환된다`() =
        runTest {
            // given
            val keyword = "실용주의 프로그래머"
            coEvery { bookRepository.fetchBooks(Keyword(keyword)) } returns
                NetworkResult.Success(
                    books,
                )

            // when
            viewModel.searchWithCurrentKeyword(keyword)

            // then
            val actual = viewModel.uiState.getOrAwaitValue().status
            val expected = SearchedBookStatus.Success

            assertEquals(expected, actual)
        }

    @Test
    fun `도서를 불러왔을 때, 값을 불러오는 중이면 로딩 상태로 전환된다`() =
        runTest {
            // given
            val keyword = "코드 작성 가이드"
            coEvery { bookRepository.fetchBooks(Keyword(keyword)) } coAnswers {
                delay(1000)
                NetworkResult.Success(
                    books,
                )
            }

            // when
            viewModel.searchWithCurrentKeyword(keyword)

            // then
            val actual = viewModel.uiState.getOrAwaitValue().status
            val expected = SearchedBookStatus.Loading

            assertEquals(expected, actual)
        }
}
