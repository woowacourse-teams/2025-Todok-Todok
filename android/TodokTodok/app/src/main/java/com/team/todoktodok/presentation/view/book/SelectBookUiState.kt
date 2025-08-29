package com.team.todoktodok.presentation.view.book

import com.team.domain.model.book.AladinBook
import com.team.domain.model.book.AladinBooks

data class SelectBookUiState(
    val isLoading: Boolean = false,
    val keyword: String = "",
    val searchedBooks: AladinBooks = AladinBooks(emptyList()),
) {
    fun selectedBook(position: Int): AladinBook? {
        if (isExist(position)) return searchedBooks[position]
        return null
    }

    fun isExist(position: Int): Boolean = searchedBooks.isExist(position)

    fun isSameKeyword(keyword: String): Boolean = this.keyword == keyword
}
