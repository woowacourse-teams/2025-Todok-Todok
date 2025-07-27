package com.team.todoktodok.presentation.view.searchbooks

import com.team.todoktodok.state.BookState

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
