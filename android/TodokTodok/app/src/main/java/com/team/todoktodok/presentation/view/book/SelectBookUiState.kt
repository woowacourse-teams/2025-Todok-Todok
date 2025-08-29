package com.team.todoktodok.presentation.view.book

import com.team.domain.model.book.AladinBook
import com.team.domain.model.book.AladinBooks

data class SelectBookUiState(
    val keyword: String = "",
    val searchedBooks: AladinBooks = AladinBooks(emptyList()),
    val status: SearchedBookResultStatus = SearchedBookResultStatus.NotStarted,
) {
    fun selectedBook(position: Int): AladinBook? {
        if (isExist(position)) return searchedBooks[position]
        return null
    }

    fun isSameKeyword(keyword: String): Boolean = this.keyword == keyword

    private fun isExist(position: Int): Boolean = searchedBooks.isExist(position)
}
