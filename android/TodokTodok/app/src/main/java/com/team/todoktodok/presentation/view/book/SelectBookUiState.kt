package com.team.todoktodok.presentation.view.book

import com.team.domain.model.book.Keyword
import com.team.domain.model.book.SearchedBook
import com.team.domain.model.book.SearchedBooksResult
import com.team.domain.model.book.map
import com.team.todoktodok.presentation.view.book.adapter.SearchBooksGroup

data class SelectBookUiState(
    val keyword: Keyword? = null,
    val searchedBooksResult: SearchedBooksResult? = null,
    val status: SearchedBookStatus = SearchedBookStatus.NotStarted,
) {
    val searchBookGroup: List<SearchBooksGroup> =
        listOf(
            SearchBooksGroup.Count(size),
            *
                searchedBooksResult?.books?.map { book -> SearchBooksGroup.Book(book) }?.toTypedArray()
                    ?: emptyArray(),
        )

    val size: Int get() = searchedBooksResult?.totalSize ?: 0

    fun selectedBook(position: Int): SearchedBook? {
        if (isExist(position)) return searchedBooksResult?.books[position]
        return null
    }

    fun isSameKeyword(value: String): Boolean = this.keyword == Keyword(value)

    private fun isExist(position: Int): Boolean = searchedBooksResult?.books?.contains(position) ?: false
}
