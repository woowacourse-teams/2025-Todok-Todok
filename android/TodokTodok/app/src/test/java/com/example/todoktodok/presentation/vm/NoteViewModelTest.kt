package com.example.todoktodok.presentation.vm

import com.example.domain.repository.BookRepository
import com.example.domain.repository.NoteRepository
import com.example.todoktodok.CoroutinesTestExtension
import com.example.todoktodok.InstantTaskExecutorExtension
import com.example.todoktodok.fixture.BOOKS_FIXTURES
import com.example.todoktodok.presentation.view.note.NoteUiEvent
import com.example.todoktodok.presentation.view.note.vm.NoteViewModel
import com.example.todoktodok.presentation.view.serialization.SerializationBook
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class NoteViewModelTest {
    private lateinit var bookRepository: BookRepository
    private lateinit var noteRepository: NoteRepository
    private lateinit var viewModel: NoteViewModel

    private val result =
        BOOKS_FIXTURES.map {
            SerializationBook(
                id = it.id,
                title = it.title,
                author = it.author,
                image = it.image,
            )
        }

    private val showOwnBooksEvent = NoteUiEvent.ShowOwnBooks(result)

    @BeforeEach
    fun setup() {
        bookRepository = mockk()
        noteRepository = mockk()
        viewModel = NoteViewModel(bookRepository, noteRepository)
    }

    @Test
    fun `내 서재에 저장된 책에 대한 정보가 없으면 getBooks가 호출된다`() =
        runTest {
            // given
            coEvery { bookRepository.getBooks() } returns BOOKS_FIXTURES

            // when
            viewModel.loadOrShowSavedBooks()

            // then
            coVerify(exactly = 1) { bookRepository.getBooks() }
            assertThat(viewModel.uiEvent.getValue()).isEqualTo(showOwnBooksEvent)
        }

    @Test
    fun `내 서재에 저장된 책에 대한 정보가 있으면 getBooks가 호출되지 않는다`() =
        runTest {
            // given
            viewModel.uiState.value = viewModel.uiState.value?.copy(savedBooks = BOOKS_FIXTURES)

            // when
            viewModel.loadOrShowSavedBooks()

            // then
            coVerify(exactly = 0) { bookRepository.getBooks() }

            assertThat(viewModel.uiEvent.getValue()).isEqualTo(showOwnBooksEvent)
        }
}
