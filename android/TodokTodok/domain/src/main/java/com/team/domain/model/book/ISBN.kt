package com.team.domain.model.book

import com.team.domain.model.exception.ISBNException

@JvmInline
value class ISBN(
    val value: String,
) {
    init {
        require(value.length in ISBN_LENGTH_BEFORE_2007..ISBN_LENGTH_AFTER_2007) { ISBNException.InvalidLength.message }
        require(
            value
                .map { it.digitToIntOrNull() != null }
                .all { it == true },
        ) { ISBNException.InvalidFormat.message }
    }

    companion object {
        private const val ISBN_LENGTH_BEFORE_2007: Int = 10
        private const val ISBN_LENGTH_AFTER_2007: Int = 13
    }
}
