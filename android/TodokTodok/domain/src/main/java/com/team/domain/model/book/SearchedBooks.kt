package com.team.domain.model.book

class SearchedBooks(
    val value: List<SearchedBook>,
) {
    val size get() = value.size

    fun isEmpty(): Boolean = value.isEmpty()

    operator fun get(position: Int): SearchedBook = value[position]

    operator fun contains(position: Int): Boolean = position in value.indices

    operator fun plus(books: SearchedBooks): SearchedBooks = SearchedBooks(value + books.value)
}

inline fun <R> SearchedBooks.map(transform: (SearchedBook) -> R): List<R> = this.value.map(transform)
