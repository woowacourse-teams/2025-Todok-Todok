package com.team.todoktodok.presentation.view.book

import com.team.domain.model.book.AladinBook
import com.team.domain.model.book.AladinBooks
import com.team.domain.model.book.map
import com.team.todoktodok.presentation.view.book.adapter.SearchBooksGroup

data class SelectBookUiState(
    val keyword: String = "",
    val searchedBooks: AladinBooks = AladinBooks(emptyList()),
    val status: SearchedBookResultStatus = SearchedBookResultStatus.NotStarted,
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

    fun isSameKeyword(keyword: String): Boolean = this.keyword == keyword

    private fun isExist(position: Int): Boolean = searchedBooks.contains(position)
}
