package com.example.todoktodok.presentation.view.searchbooks.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Book
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

    private val _books: MutableLiveData<List<Book>> = MutableLiveData(emptyList())
    val books: LiveData<List<Book>> get() = _books

    private val _searchInput: MutableLiveData<String?> = MutableLiveData()
    val searchInput: LiveData<String?> get() = _searchInput

    fun saveBook(position: Int) {
        val book = _books.value?.get(position) ?: return
        viewModelScope.launch {
            bookRepository.saveBook(book)
        }
    }

    fun updateSearchInput(searchInput: String) {
        _searchInput.value = searchInput
    }

    fun updateSelectedBook(selectedPosition: Int) {
        val currentState = _uiState.value ?: SearchBooksUiState()
        val updatedState = currentState.findSelectedBook(selectedPosition)
        _uiState.value = updatedState
    }

    fun searchBooks() {
        val input = _searchInput.value ?: return
        viewModelScope.launch {
            val books = bookRepository.getBooks(input)
            if (books.isEmpty()) return@launch
            _books.value = books
        }
    }
}
