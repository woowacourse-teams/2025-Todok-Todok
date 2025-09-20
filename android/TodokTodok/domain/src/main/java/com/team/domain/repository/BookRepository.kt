package com.team.domain.repository

import com.team.domain.model.Book
import com.team.domain.model.book.Keyword
import com.team.domain.model.book.SearchedBooksResult
import com.team.domain.model.exception.NetworkResult

interface BookRepository {
    suspend fun fetchBooks(
        size: Int,
        keyword: Keyword,
    ): NetworkResult<SearchedBooksResult>

    suspend fun saveBook(book: Book): NetworkResult<Long>
}
