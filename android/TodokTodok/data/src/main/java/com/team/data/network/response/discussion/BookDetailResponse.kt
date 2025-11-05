package com.team.data.network.response.discussion

import com.team.domain.model.book.BookDetail
import kotlinx.serialization.Serializable

@Serializable
data class BookDetailResponse(
    val bookAuthor: String,
    val bookId: Long,
    val bookImage: String,
    val bookTitle: String,
    val bookPublisher: String,
    val bookSummary: String,
)

fun BookDetailResponse.toDomain() =
    BookDetail(
        id = bookId,
        title = bookTitle,
        author = bookAuthor,
        image = bookImage,
        publisher = bookPublisher,
        summary = bookSummary,
    )
