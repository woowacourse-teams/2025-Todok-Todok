package com.team.data.network.request

import com.team.domain.model.book.SearchedBook
import kotlinx.serialization.Serializable

@Serializable
data class BookRequest(
    val bookIsbn: Long,
    val bookTitle: String,
    val bookAuthor: String,
    val bookImage: String,
)

fun SearchedBook.toRequest() =
    BookRequest(
        bookIsbn = isbn,
        bookTitle = title,
        bookAuthor = author,
        bookImage = image,
    )
