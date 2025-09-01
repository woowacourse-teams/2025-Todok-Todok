package com.team.todoktodok.data.network.response.book

import com.team.domain.model.book.AladinBook.Companion.AladinBook
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AladinBookResponse(
    @SerialName("bookId")
    val isbn: Long,
    @SerialName("bookTitle")
    val title: String,
    @SerialName("bookAuthor")
    val author: String,
    @SerialName("bookImage")
    val image: String,
)

fun AladinBookResponse.toDomain() =
    AladinBook(
        isbn = isbn,
        title = title,
        author = author,
        image = image,
    )
