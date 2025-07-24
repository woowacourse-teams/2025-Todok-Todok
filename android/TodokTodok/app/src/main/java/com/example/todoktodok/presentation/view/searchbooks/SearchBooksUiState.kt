package com.example.todoktodok.presentation.view.searchbooks

import com.example.domain.model.Book
import com.example.todoktodok.state.BookState

data class SearchBooksUiState(
    val isLoading: Boolean = false,
    val searchInput: String? = null,
    val selectedBook: BookState? = null,
    val searchedBooks: List<BookState> = emptyList(),
    val errorMessage: String? = null,
) {
    fun findSelectedBook(selectedPosition: Int): SearchBooksUiState {
        val selectedBook =
            searchedBooks[selectedPosition]
        return this.copy(selectedBook = selectedBook)
    }
}
