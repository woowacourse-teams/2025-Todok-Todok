package com.team.todoktodok.presentation.view.book.adapter

import com.team.domain.model.book.AladinBook
import com.team.todoktodok.R

sealed class SearchBooksGroup(
    val viewType: Int,
) {
    data class Count(
        val size: Int,
    ) : SearchBooksGroup(R.layout.item_searched_books_count)

    data class Book(
        val book: AladinBook,
    ) : SearchBooksGroup(R.layout.item_book)
}
