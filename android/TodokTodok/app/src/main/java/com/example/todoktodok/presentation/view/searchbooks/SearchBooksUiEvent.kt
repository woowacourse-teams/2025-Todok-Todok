package com.example.todoktodok.presentation.view.searchbooks

import com.example.domain.model.Book
import com.example.todoktodok.state.BookState

sealed interface SearchBooksUiEvent {
    data object NavigateToLibrary : SearchBooksUiEvent

    data class ShowSearchedBooks(
        val books: List<BookState>,
    ) : SearchBooksUiEvent

    data class ShowDialog(
        val message: String,
    ) : SearchBooksUiEvent
}
