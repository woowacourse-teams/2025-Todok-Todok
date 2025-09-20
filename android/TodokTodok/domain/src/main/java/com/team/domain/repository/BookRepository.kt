package com.team.domain.repository

import com.team.domain.model.Book
import com.team.domain.model.book.Keyword
import com.team.domain.model.book.SearchedBooks
import com.team.domain.model.exception.NetworkResult

interface BookRepository {
    suspend fun fetchBooks(keyword: Keyword): NetworkResult<SearchedBooks>

    suspend fun saveBook(book: Book): NetworkResult<Long>
}
