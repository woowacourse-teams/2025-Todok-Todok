package com.team.domain.repository

import com.team.domain.model.Book
import com.team.domain.model.book.AladinBooks
import com.team.domain.model.exception.NetworkResult

interface BookRepository {
    suspend fun fetchBooks(keyword: String): NetworkResult<AladinBooks>

    suspend fun saveBook(book: Book): NetworkResult<Long>
}
