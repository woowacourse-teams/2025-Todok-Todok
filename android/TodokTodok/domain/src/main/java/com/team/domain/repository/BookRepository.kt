package com.team.domain.repository

import com.team.domain.model.Book
import com.team.domain.model.book.AladinBooks
import com.team.domain.model.book.Keyword
import com.team.domain.model.exception.NetworkResult

interface BookRepository {
    suspend fun fetchBooks(keyword: Keyword): NetworkResult<AladinBooks>

    suspend fun saveBook(book: Book): NetworkResult<Long>
}
