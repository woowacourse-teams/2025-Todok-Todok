package com.team.todoktodok.presentation.compose.bookdiscussions.model

import androidx.compose.runtime.Immutable
import com.team.domain.model.Book

@Immutable
data class BookDetailSectionUiState(
    val bookTitle: String,
    val bookAuthor: String,
    val bookImage: String,
    val bookPublisher: String,
    val bookSummary: String,
)

fun Book.toBookDetailUiState() =
    BookDetailSectionUiState(
        title,
        author,
        image,
        publisher,
        summary,
    )
