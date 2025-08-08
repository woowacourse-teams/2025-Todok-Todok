package com.team.todoktodok.data.network.request

import com.team.domain.model.Book
import kotlinx.serialization.Serializable

@Serializable
data class BookRequest(
    val bookId: Long,
    val bookTitle: String,
    val bookAuthor: String,
    val bookImage: String,
)

fun Book.toRequest() =
    BookRequest(
        bookId = id,
        bookTitle = title,
        bookAuthor = author,
        bookImage = image,
    )