package com.example.todoktodok.data.datasource

import com.example.domain.model.Book

interface BookDataSource {
    suspend fun fetchBooks(): List<Book>

    suspend fun fetchBooks(searchInput: String): List<Book>

    suspend fun saveBook(bookId: Long)
}
