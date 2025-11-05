package com.team.data.network.response.discussion

import com.team.domain.model.Book
import kotlinx.serialization.Serializable

@Serializable
data class BookResponse(
    val bookAuthor: String,
    val bookId: Long,
    val bookImage: String,
    val bookTitle: String,
)

fun BookResponse.toDomain() =
    Book(
        id = bookId,
        title = bookTitle,
        author = bookAuthor,
        image = bookImage,
    )
