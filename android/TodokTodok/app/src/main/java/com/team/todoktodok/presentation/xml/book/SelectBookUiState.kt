package com.team.todoktodok.presentation.xml.book

import com.team.domain.model.book.AladinBook
import com.team.domain.model.book.AladinBooks
import com.team.domain.model.book.Keyword
import com.team.domain.model.book.map
import com.team.todoktodok.presentation.xml.book.adapter.SearchBooksGroup

data class SelectBookUiState(
    val keyword: Keyword? = null,
    val searchedBooks: AladinBooks = AladinBooks(emptyList()),
    val status: SearchedBookStatus = SearchedBookStatus.NotStarted,
) {
    val searchBookGroup: List<SearchBooksGroup> =
        listOf(
            SearchBooksGroup.Count(size),
            *searchedBooks.map { SearchBooksGroup.Book(it) }.toTypedArray(),
        )

    val size: Int get() = searchedBooks.size

    fun selectedBook(position: Int): AladinBook? {
        if (isExist(position)) return searchedBooks[position]
        return null
    }

    fun isSameKeyword(value: String): Boolean = this.keyword == Keyword(value)

    private fun isExist(position: Int): Boolean = searchedBooks.contains(position)
}
