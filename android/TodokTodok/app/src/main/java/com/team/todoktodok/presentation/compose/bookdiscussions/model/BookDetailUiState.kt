package com.team.todoktodok.presentation.compose.bookdiscussions.model

import com.team.domain.model.Book

data class BookDetailUiState(
    val bookTitle: String,
    val bookAuthor: String,
    val bookImage: String,
    val bookPublisher: String,
    val bookSummary: String,
)

fun Book.toBookDetailUiState() =
    BookDetailUiState(
        title,
        author,
        image,
        publisher,
        summary,
    )
