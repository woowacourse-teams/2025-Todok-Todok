package com.team.todoktodok.data.repository

import com.team.domain.model.Book
import com.team.domain.model.book.Keyword
import com.team.domain.model.book.SearchedBooks
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.map
import com.team.domain.repository.BookRepository
import com.team.todoktodok.data.datasource.book.BookRemoteDataSource
import com.team.todoktodok.data.network.request.toRequest
import com.team.todoktodok.data.network.response.book.SearchedBookResponse
import com.team.todoktodok.data.network.response.book.toDomain

class DefaultBookRepository(
    private val bookRemoteDataSource: BookRemoteDataSource,
) : BookRepository {
    override suspend fun fetchBooks(keyword: Keyword): NetworkResult<SearchedBooks> =
        bookRemoteDataSource
            .fetchBooks(keyword.value)
            .map { SearchedBookResponse: List<SearchedBookResponse> ->
                SearchedBooks(SearchedBookResponse.map { it.toDomain() })
            }

    override suspend fun saveBook(book: Book): NetworkResult<Long> = bookRemoteDataSource.saveBook(book.toRequest())
}
