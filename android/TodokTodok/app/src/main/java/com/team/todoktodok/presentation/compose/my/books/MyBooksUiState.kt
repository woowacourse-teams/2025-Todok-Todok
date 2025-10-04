package com.team.todoktodok.presentation.compose.my.books

import com.team.domain.model.Book

data class MyBooksUiState(
    val books: List<Book> = emptyList(),
) {
    val notHasBooks = books.isEmpty()
}
