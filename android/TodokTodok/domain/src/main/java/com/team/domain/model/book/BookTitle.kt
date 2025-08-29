package com.team.domain.model.book

import com.team.domain.model.exception.BookTitleException

@JvmInline
value class BookTitle(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { BookTitleException.EmptyBookTitle.message }
    }

    val mainTitle: String get() = value.split(SPLIT_STANDARD)[0]

    val subTitle: String get() = value.split(SPLIT_STANDARD)[1]

    companion object {
        private const val SPLIT_STANDARD = " - "
    }
}
