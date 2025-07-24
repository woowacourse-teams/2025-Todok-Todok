package com.example.todoktodok.presentation.view.searchbooks

import com.example.domain.model.Book

data class SearchBooksUiState(
    val isLoading: Boolean = false,
    val searchInput: String? = null,
    val selectedBook: Book? = null,
    val searchedBooks: List<Book> = emptyList(),
    val errorMessage: String? = null,
) {
    fun findSelectedBook(selectedPosition: Int): SearchBooksUiState {
        val selectedBook =
            searchedBooks[selectedPosition]
        return this.copy(selectedBook = selectedBook)
    }
}
