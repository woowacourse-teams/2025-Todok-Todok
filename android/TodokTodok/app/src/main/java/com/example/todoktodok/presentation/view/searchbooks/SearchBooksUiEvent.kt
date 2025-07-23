package com.example.todoktodok.presentation.view.searchbooks

import com.example.domain.model.Book

sealed interface SearchBooksUiEvent {
    data object NavigateToLibrary : SearchBooksUiEvent
    data class ShowSearchedBooks(
        val books: List<Book>,
    ) : SearchBooksUiEvent

    data class ShowDialog(
        val message: String,
    ) : SearchBooksUiEvent
}
