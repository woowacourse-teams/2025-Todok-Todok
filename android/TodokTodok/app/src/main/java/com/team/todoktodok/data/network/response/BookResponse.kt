package com.team.todoktodok.data.network.response

import com.team.domain.model.Book
import kotlinx.serialization.Serializable

@Serializable
data class BookResponse(
    val bookId: Long,
    val bookTitle: String,
    val bookAuthor: String,
    val bookImage: String,
) {
    fun toDomain(): Book =
        Book(
            id = this.bookId,
            title = this.bookTitle,
            author = this.bookAuthor,
            image = this.bookImage,
        )
}
