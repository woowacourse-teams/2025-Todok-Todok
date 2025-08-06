package com.team.todoktodok.data.network.request

import com.team.domain.model.Book
import kotlinx.serialization.Serializable

@Serializable
data class BookRequest(
    val bookIsbn: Long,
    val bookTitle: String,
    val bookAuthor: String,
    val bookImage: String,
)

fun Book.toRequest() = BookRequest(
    bookIsbn = id,
    bookTitle = title,
    bookAuthor = author,
    bookImage = image,
)