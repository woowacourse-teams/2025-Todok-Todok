package com.team.domain.model.book

import com.team.domain.model.exception.ISBNException

@JvmInline
value class ISBN(
    val value: Long,
) {
    init {
        require(value.toString().length == ISBN_LENGTH_BEFORE_2007 || value.toString().length == ISBN_LENGTH_AFTER_2007) {
            ISBNException.InvalidLength.message
        }
    }

    companion object {
        private const val ISBN_LENGTH_BEFORE_2007: Int = 10
        private const val ISBN_LENGTH_AFTER_2007: Int = 13
    }
}
