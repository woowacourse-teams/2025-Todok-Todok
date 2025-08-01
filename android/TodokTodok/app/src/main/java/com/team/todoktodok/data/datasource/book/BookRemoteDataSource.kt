package com.team.todoktodok.data.datasource.book

import com.team.domain.model.Book

interface BookRemoteDataSource {
    suspend fun fetchBooks(): List<Book>

    suspend fun fetchBooks(searchInput: String): List<Book>

    suspend fun saveBook(bookId: Long)

    suspend fun saveSelectedBook(book: Book): Long
}
