package com.team.todoktodok.presentation.view.book

import com.team.domain.model.book.Keyword
import com.team.domain.model.book.SearchedBook
import com.team.domain.model.book.SearchedBooksResult
import com.team.domain.model.book.map
import com.team.todoktodok.presentation.view.book.adapter.SearchBooksGroup

data class SelectBookUiState(
    val pageSize: Int = 0,
    val keyword: Keyword? = null,
    val searchedBooksResult: SearchedBooksResult? = null,
    val status: SearchedBookStatus = SearchedBookStatus.NotStarted,
) {
    val hasNextPage: Boolean
        get() = searchedBooksResult?.hasNext ?: false
    val searchBookGroup: List<SearchBooksGroup>
        get() =
            listOf(
                SearchBooksGroup.Count(size),
                *
                    searchedBooksResult
                        ?.books
                        ?.map { book -> SearchBooksGroup.Book(book) }
                        ?.toTypedArray()
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
