package com.team.todoktodok.data.repository

import com.team.domain.model.Book
import com.team.domain.model.Books
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.map
import com.team.domain.repository.BookRepository
import com.team.todoktodok.data.datasource.book.BookRemoteDataSource
import com.team.todoktodok.data.network.request.toRequest
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.response.discussion.toDomain

class DefaultBookRepository(
    private val bookRemoteDataSource: BookRemoteDataSource,
) : BookRepository {
    override suspend fun fetchBooks(keyword: String): NetworkResult<Books> =
        bookRemoteDataSource
            .fetchBooks(keyword)
            .map { bookResponse: List<BookResponse> -> Books(bookResponse.map { it.toDomain() }) }

    override suspend fun saveBook(book: Book): NetworkResult<Long> = bookRemoteDataSource.saveBook(book.toRequest())
}
