package com.team.todoktodok.data.network.response.discussion

import com.team.domain.model.Book
import kotlinx.serialization.Serializable

@Serializable
data class BookResponse(
    val bookAuthor: String,
    val bookId: Long,
    val bookImage: String,
    val bookTitle: String,
    val bookPublisher: String = "",
    val bookSummary: String = "",
)

fun BookResponse.toDomain() =
    Book(
        id = bookId,
        title = bookTitle,
        author = bookAuthor,
        image = bookImage,
        publisher = bookPublisher,
        summary = bookSummary,
    )
