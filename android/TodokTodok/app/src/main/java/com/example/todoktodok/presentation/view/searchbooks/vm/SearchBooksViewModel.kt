package com.example.todoktodok.presentation.view.searchbooks.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Book
import com.example.domain.repository.BookRepository
import com.example.todoktodok.presentation.view.searchbooks.SearchBooksUiEvent
import com.example.todoktodok.presentation.view.searchbooks.SearchBooksUiState
import com.example.todoktodok.state.BookState
import com.example.todoktodok.state.toDomain
import kotlinx.coroutines.launch

class SearchBooksViewModel(
    private val bookRepository: BookRepository,
) : ViewModel() {
    private var uiState: SearchBooksUiState = SearchBooksUiState()

    private val _uiEvent: MutableLiveData<SearchBooksUiEvent> = MutableLiveData()
    val uiEvent: LiveData<SearchBooksUiEvent> get() = _uiEvent

    fun updateSearchInput(searchInput: String) {
        val currentState = uiState
        val updatedState = currentState.copy(searchInput = searchInput)
        uiState = updatedState
    }

    fun searchBooks() {
        val input = uiState.searchInput.orEmpty()
        if (input.isEmpty()) {
            _uiEvent.value = SearchBooksUiEvent.ShowDialog(ERROR_EMPTY_SEARCH_INPUT)
            return
        }

        uiState = uiState.copy(isLoading = true)

        viewModelScope.launch {
            val books = bookRepository.searchBooks(input)
            val booksState =
                books.map { book: Book -> BookState(book.id, book.title, book.author, book.image) }
            if (booksState.isEmpty()) {
                _uiEvent.value = SearchBooksUiEvent.ShowDialog(ERROR_NO_SEARCH_RESULTS)
                return@launch
            }
            uiState = uiState.copy(isLoading = false, searchedBooks = booksState)
            _uiEvent.value = SearchBooksUiEvent.ShowSearchedBooks(booksState)
        }
    }

    fun navigateToLibrary() {
        _uiEvent.value = _uiEvent.value ?: SearchBooksUiEvent.NavigateToLibrary
    }

    fun updateSelectedBook(selectedPosition: Int) {
        val currentState = uiState
        val updatedState = currentState.findSelectedBook(selectedPosition)
        if (updatedState.selectedBook == null) {
            _uiEvent.value = _uiEvent.value ?: SearchBooksUiEvent.ShowDialog(ERROR_BOOK_NOT_FOUND)
            return
        }
        uiState = updatedState
    }

    fun saveSelectedBook() {
        val selectedBook: Book? = uiState.selectedBook?.toDomain()
        if (selectedBook == null) {
            _uiEvent.value = _uiEvent.value ?: SearchBooksUiEvent.ShowDialog(ERROR_NO_SELECTED_BOOK)
            return
        }

        viewModelScope.launch {
            bookRepository.saveBook(selectedBook)
            _uiEvent.value = _uiEvent.value ?: SearchBooksUiEvent.ShowDialog(MESSAGE_BOOK_SAVED)
            _uiEvent.value = _uiEvent.value ?: SearchBooksUiEvent.NavigateToLibrary
        }
    }

    fun clearUiState() {
        _uiEvent.value = null
    }

    companion object {
        private const val MESSAGE_BOOK_SAVED: String = "책이 저장되었습니다"
        private const val ERROR_NO_SELECTED_BOOK: String = "선택된 책이 없습니다"
        private const val ERROR_BOOK_NOT_FOUND: String = "책을 찾을 수 없습니다"
        private const val ERROR_NO_SEARCH_RESULTS: String = "검색 결과가 없습니다"
        private const val ERROR_EMPTY_SEARCH_INPUT: String = "검색어를 입력해주세요"
    }
}
