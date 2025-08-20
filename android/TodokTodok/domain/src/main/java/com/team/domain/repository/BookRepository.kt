package com.team.domain.repository

import com.team.domain.model.Book
import com.team.domain.model.Books
import com.team.domain.model.exception.NetworkResult

interface BookRepository {
    suspend fun fetchBooks(keyword: String): NetworkResult<Books>

    suspend fun saveBook(book: Book): NetworkResult<Long>
}
