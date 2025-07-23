package com.example.todoktodok.presentation.view.searchbooks

sealed interface SearchBooksUiEvent {
    data object NavigateToLibrary : SearchBooksUiEvent
    data class ShowDialog(val message: String) : SearchBooksUiEvent
}