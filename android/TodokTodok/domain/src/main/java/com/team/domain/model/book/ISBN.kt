package com.team.domain.model.book

import com.team.domain.model.exception.BookException
import com.team.domain.model.exception.ISBNException

@JvmInline
value class ISBN(
    val value: String,
) {
    init {
        require(value.length == ISBN_LENGTH) { ISBNException.InvalidLength.message }
        require(value.map { it.digitToIntOrNull() != null }
            .all { it == true }) { ISBNException.InvalidFormat.message }
    }

    companion object {
        private const val ISBN_LENGTH: Int = 13
    }
}