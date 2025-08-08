package com.team.domain.repository

import com.team.domain.model.Book
import com.team.domain.model.Books

interface BookRepository {
    suspend fun fetchBooks(keyword: String): Books

    suspend fun saveBook(book: Book): Long
}
