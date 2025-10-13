package com.team.todoktodok.presentation.xml.book.adapter

import com.team.domain.model.book.SearchedBook
import com.team.todoktodok.R

sealed class SearchBooksGroup(
    val viewType: Int,
) {
    data class Count(
        val size: Int,
    ) : SearchBooksGroup(R.layout.item_searched_books_count)

    data class Book(
        val book: SearchedBook,
    ) : SearchBooksGroup(R.layout.item_book)
}
