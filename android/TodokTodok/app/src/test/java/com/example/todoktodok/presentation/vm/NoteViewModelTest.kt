package com.example.todoktodok.presentation.vm

import androidx.lifecycle.MutableLiveData
import com.example.domain.repository.BookRepository
import com.example.domain.repository.NoteRepository
import com.example.todoktodok.CoroutinesTestExtension
import com.example.todoktodok.InstantTaskExecutorExtension
import com.example.todoktodok.ext.getOrAwaitValue
import com.example.todoktodok.fixture.BOOKS_FIXTURES
import com.example.todoktodok.presentation.view.note.NoteState
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
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.lang.reflect.Field

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class NoteViewModelTest {
    private lateinit var bookRepository: BookRepository
    private lateinit var noteRepository: NoteRepository
    private lateinit var viewModel: NoteViewModel

    private val testBook = BOOKS_FIXTURES.items.first()
    private val testSelectedBookState =
        NoteState(selectedBook = testBook, savedBooks = BOOKS_FIXTURES)

    private val result =
        BOOKS_FIXTURES.items.map {
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
        bookRepository = mockk(relaxed = true)
        noteRepository = mockk(relaxed = true)
        viewModel = NoteViewModel(bookRepository, noteRepository)
    }

    @Nested
    @DisplayName("loadOrShowSavedBooks 함수는")
    inner class DescribeLoadOrShowSavedBooks {
        @Test
        @DisplayName("내 서재에 저장된 책 정보가 없으면 getBooks를 호출하고 ShowOwnBooks 이벤트를 발생시킨다")
        fun callsGetBooksAndEmitsShowOwnBooksEvent() =
            runTest {
                // given
                coEvery { bookRepository.getBooks() } returns BOOKS_FIXTURES

                // when
                viewModel.loadOrShowSavedBooks()

                // then
                coVerify(exactly = 1) { bookRepository.getBooks() }
                assertThat(viewModel.uiEvent.getOrAwaitValue()).isEqualTo(showOwnBooksEvent)
                assertThat(viewModel.uiState.getOrAwaitValue().savedBooks).isEqualTo(BOOKS_FIXTURES)
            }

        @Test
        @DisplayName("내 서재에 저장된 책 정보가 있으면 getBooks를 호출하지 않고 ShowOwnBooks 이벤트를 발생시킨다")
        fun doesNotCallGetBooksAndEmitsShowOwnBooksEvent() =
            runTest {
                // given
                setViewModelUiState(NoteState(savedBooks = BOOKS_FIXTURES))

                // when
                viewModel.loadOrShowSavedBooks()

                // then
                coVerify(exactly = 0) { bookRepository.getBooks() }
                assertThat(viewModel.uiEvent.getOrAwaitValue()).isEqualTo(showOwnBooksEvent)
            }
    }

    @Nested
    @DisplayName("saveNote 함수는")
    inner class DescribeSaveNote {
        private val testSnap = "테스트 스냅입니다."
        private val testMemo = "테스트 메모입니다."

        @Test
        @DisplayName("책이 선택되어 있고 snap과 memo가 있으면 noteRepository의 saveNote를 호출한다")
        fun callsRepositorySaveNote() =
            runTest {
                // given
                setViewModelUiState(testSelectedBookState.copy(snap = testSnap, memo = testMemo))
                coEvery { noteRepository.saveNote(any(), any(), any()) } returns Unit

                // when
                viewModel.saveNote()

                // then
                coVerify(exactly = 1) {
                    noteRepository.saveNote(testBook.id, testSnap, testMemo)
                }
            }

        @Test
        @DisplayName("책이 선택되어 있지 않으면 NotHasSelectedBook 이벤트를 발생시키고 저장된 책을 로드한다")
        fun emitsNotHasSelectedBookEventAndLoadsBooks() =
            runTest {
                // given
                setViewModelUiState(
                    NoteState(
                        selectedBook = null,
                        savedBooks = null,
                    ),
                )
                coEvery { bookRepository.getBooks() } returns BOOKS_FIXTURES

                // when
                viewModel.saveNote()

                // then
                assertThat(viewModel.uiEvent.getOrAwaitValue()).isEqualTo(showOwnBooksEvent)

                coVerify(exactly = 1) { bookRepository.getBooks() }
            }
    }

    @Test
    @DisplayName("updateSnap 함수는 uiState의 snap 값을 업데이트한다")
    fun updateSnap_updatesSnapInUiState() {
        // given
        val newSnap = "새로운 스냅 내용"

        // when
        viewModel.updateSnap(newSnap)

        // then
        assertThat(viewModel.uiState.getOrAwaitValue().snap).isEqualTo(newSnap)
    }

    @Test
    @DisplayName("updateMemo 함수는 uiState의 memo 값을 업데이트한다")
    fun updateMemo_updatesMemoInUiState() {
        // given
        val newMemo = "새로운 메모 내용"

        // when
        viewModel.updateMemo(newMemo)

        // then
        assertThat(viewModel.uiState.getOrAwaitValue().memo).isEqualTo(newMemo)
    }

    private fun setViewModelUiState(state: NoteState) {
        val uiStateField: Field = NoteViewModel::class.java.getDeclaredField("_uiState")
        uiStateField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val uiState = uiStateField.get(viewModel) as MutableLiveData<NoteState>
        uiState.value = state
    }
}
