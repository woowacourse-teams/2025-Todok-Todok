package com.team.todoktodok.presentation.compose.my.books

import com.google.common.collect.ImmutableList
import com.team.domain.model.Book

data class MyBooksUiModel(
    val books: ImmutableList<Book> = ImmutableList.of(),
) {
    fun setBooks(updatedBooks: List<Book>): MyBooksUiModel = copy(books = ImmutableList.copyOf(updatedBooks))

    val notHasBooks: Boolean
        get() = books.isEmpty()

    val rows: List<ImmutableList<Book>>
        get() = books.chunked(COLUMN_SIZE).map { ImmutableList.copyOf(it) }

    companion object {
        private const val COLUMN_SIZE = 3
    }
}
