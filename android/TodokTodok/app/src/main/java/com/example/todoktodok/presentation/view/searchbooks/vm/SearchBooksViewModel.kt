package com.example.todoktodok.presentation.view.searchbooks.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.BookRepository
import com.example.todoktodok.presentation.view.searchbooks.SearchBooksUiEvent
import com.example.todoktodok.presentation.view.searchbooks.SearchBooksUiState
import kotlinx.coroutines.launch

class SearchBooksViewModel(
    private val bookRepository: BookRepository,
) : ViewModel() {
    private val _uiState: MutableLiveData<SearchBooksUiState> = MutableLiveData()
    val uiState: LiveData<SearchBooksUiState> get() = _uiState

    private val _uiEvent: MutableLiveData<SearchBooksUiEvent> = MutableLiveData()
    val uiEvent: LiveData<SearchBooksUiEvent> get() = _uiEvent

    fun updateSearchInput(searchInput: String) {
        val currentState = _uiState.value ?: SearchBooksUiState()
        val updatedState = currentState.copy(searchInput = searchInput)
        _uiState.value = updatedState
    }

    fun searchBooks() {
        val input = _uiState.value?.searchInput.orEmpty()
        if (input.isEmpty()) {
            _uiEvent.value = SearchBooksUiEvent.ShowDialog("검색어를 입력해주세요")
            return
        }

        _uiState.value = _uiState.value?.copy(isLoading = true)

        viewModelScope.launch {
            val books = bookRepository.searchBooks(input)
            if (books.isEmpty()) {
                _uiEvent.value = SearchBooksUiEvent.ShowDialog("검색 결과가 없습니다")
                return@launch
            }
            _uiState.value = _uiState.value?.copy(isLoading = false, searchedBooks = books)
            _uiEvent.value = _uiEvent.value ?: SearchBooksUiEvent.ShowSearchedBooks(books)
        }
    }

    fun navigateToLibrary() {
        _uiEvent.value = _uiEvent.value ?: SearchBooksUiEvent.NavigateToLibrary
    }

    fun updateSelectedBook(selectedPosition: Int) {
        val currentState = _uiState.value ?: SearchBooksUiState()
        val updatedState = currentState.findSelectedBook(selectedPosition)
        if (updatedState.selectedBook == null) {
            _uiEvent.value = _uiEvent.value ?: SearchBooksUiEvent.ShowDialog("책을 찾을 수 없습니다")
            return
        }
        _uiState.value = updatedState
    }

    fun saveSelectedBook() {
        val selectedBook = _uiState.value?.selectedBook
        if (selectedBook == null) {
            _uiEvent.value = _uiEvent.value ?: SearchBooksUiEvent.ShowDialog("선택된 책이 없습니다")
            return
        }
        viewModelScope.launch {
            bookRepository.saveBook(selectedBook)
            _uiEvent.value = _uiEvent.value ?: SearchBooksUiEvent.ShowDialog("책이 저장되었습니다")
            _uiEvent.value = _uiEvent.value ?: SearchBooksUiEvent.NavigateToLibrary
        }
    }

    fun clearUiState() {
        _uiState.value = null
    }
}
