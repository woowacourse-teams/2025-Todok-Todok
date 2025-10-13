package com.team.domain.model.book

import com.team.domain.model.exception.KeywordException

@JvmInline
value class Keyword(
    val value: String,
) {
    init {
        require(value.isNotEmpty()) { KeywordException.EmptyKeyword }
        require(value.isNotBlank()) { KeywordException.BlankKeyword }
    }
}

val Keyword.length: Int
    get() = value.length
