package com.team.todoktodok.data.network.response.discussion

import com.team.domain.model.Book
import com.team.domain.model.book.AladinBook.Companion.AladinBook
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

fun BookResponse.toAladinBook() =
    AladinBook(
        id = bookId.toString(),
        title = bookTitle,
        author = bookAuthor,
        image = bookImage,
    )
