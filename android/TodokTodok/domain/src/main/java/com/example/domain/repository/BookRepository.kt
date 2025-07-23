package com.example.domain.repository

import com.example.domain.model.Book

interface BookRepository {
    suspend fun getBooks(searchInput: String): List<Book>

    suspend fun saveBook(book: Book)
}
