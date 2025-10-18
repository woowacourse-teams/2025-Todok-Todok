package com.team.domain.model.book

import com.team.domain.model.exception.BookAuthorException

@JvmInline
value class BookAuthor(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { BookAuthorException.EmptyBookAuthor.message }
    }
}
