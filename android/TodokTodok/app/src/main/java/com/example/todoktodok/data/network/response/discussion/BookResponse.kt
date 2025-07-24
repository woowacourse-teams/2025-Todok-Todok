package com.example.todoktodok.data.network.response.discussion

import com.example.domain.model.Book
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookResponse(
    @SerialName("bookAuthor")
    val bookAuthor: String,
    @SerialName("bookId")
    val bookId: Long,
    @SerialName("bookImage")
    val bookImage: String,
    @SerialName("bookTitle")
    val bookTitle: String,
)

fun BookResponse.toDomain() =
    Book(
        id = bookId,
        title = bookTitle,
        author = bookAuthor,
        image = bookImage,
    )
