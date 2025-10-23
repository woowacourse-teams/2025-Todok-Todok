package com.team.todoktodok.data.network.response.book

import com.team.domain.model.book.SearchedBook.Companion.SearchedBook
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchedBookResponse(
    @SerialName("bookId")
    val isbn: Long,
    @SerialName("bookTitle")
    val title: String,
    @SerialName("bookAuthor")
    val author: String,
    @SerialName("bookImage")
    val image: String,
    @SerialName("bookPublisher")
    val publisher: String = "",
    @SerialName("bookSummary")
    val summary: String = "",
)

fun SearchedBookResponse.toDomain() =
    SearchedBook(
        isbn = isbn,
        title = title,
        author = author,
        image = image,
    )
