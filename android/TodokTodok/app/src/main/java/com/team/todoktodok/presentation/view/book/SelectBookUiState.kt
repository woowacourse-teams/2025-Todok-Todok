package com.team.todoktodok.presentation.view.book

import com.team.domain.model.book.Keyword
import com.team.domain.model.book.SearchedBook
import com.team.domain.model.book.SearchedBooks
import com.team.domain.model.book.map
import com.team.todoktodok.presentation.view.book.adapter.SearchBooksGroup

data class SelectBookUiState(
    val keyword: Keyword? = null,
    val searchedBooks: SearchedBooks = SearchedBooks(emptyList()),
    val status: SearchedBookStatus = SearchedBookStatus.NotStarted,
) {
    val searchBookGroup: List<SearchBooksGroup> =
        listOf(
            SearchBooksGroup.Count(size),
            *searchedBooks.map { SearchBooksGroup.Book(it) }.toTypedArray(),
        )

    val size: Int get() = searchedBooks.size

    fun selectedBook(position: Int): SearchedBook? {
        if (isExist(position)) return searchedBooks[position]
        return null
    }

    fun isSameKeyword(value: String): Boolean = this.keyword == Keyword(value)

    private fun isExist(position: Int): Boolean = searchedBooks.contains(position)
}
