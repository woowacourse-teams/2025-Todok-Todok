package com.team.ui_compose.my.books

import com.team.domain.model.Book
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class MyBooksUiModel(
    val books: ImmutableList<Book> = persistentListOf(),
) {
    fun setBooks(updatedBooks: List<Book>): MyBooksUiModel = copy(books = updatedBooks.toImmutableList())

    val notHasBooks: Boolean
        get() = books.isEmpty()

    val rows: List<ImmutableList<Book>>
        get() = books.chunked(COLUMN_SIZE).map { it.toImmutableList() }

    companion object {
        private const val COLUMN_SIZE = 3
    }
}
