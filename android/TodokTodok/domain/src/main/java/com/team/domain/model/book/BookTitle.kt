package com.team.domain.model.book

import com.team.domain.model.exception.BookTitleException

@JvmInline
value class BookTitle(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { BookTitleException.EmptyBookTitle.message }
    }
}
