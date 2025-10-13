package com.team.domain.model.book

import com.team.domain.model.exception.SearchedBooksTotalSize

@JvmInline
value class SearchedBooksTotalSize(
    val value: Int,
) {
    init {
        require(value in MIN_SEARCHED_BOOKS_SIZE..MAX_SEARCHED_BOOKS_SIZE) { SearchedBooksTotalSize.InvalidSize.message }
    }

    companion object {
        private const val MIN_SEARCHED_BOOKS_SIZE: Int = 0
        private const val MAX_SEARCHED_BOOKS_SIZE: Int = 200
    }
}
