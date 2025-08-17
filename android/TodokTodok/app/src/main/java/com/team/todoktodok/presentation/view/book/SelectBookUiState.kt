package com.team.todoktodok.presentation.view.book

import com.team.domain.model.Book
import com.team.domain.model.Books

data class SelectBookUiState(
    val isLoading: Boolean = false,
    val keyword: String = "",
    val searchedBooks: Books = Books(emptyList()),
    val selectedBook: Book? = null,
)
