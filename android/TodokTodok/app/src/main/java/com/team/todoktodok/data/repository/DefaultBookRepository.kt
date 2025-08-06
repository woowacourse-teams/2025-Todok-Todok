package com.team.todoktodok.data.repository

import com.team.domain.model.Books
import com.team.domain.repository.BookRepository
import com.team.todoktodok.data.datasource.book.BookRemoteDataSource
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.response.discussion.toDomain

class DefaultBookRepository(
    private val bookRemoteDataSource: BookRemoteDataSource,
) : BookRepository {
    override suspend fun fetchBooks(keyword: String): Books {
        val value =
            bookRemoteDataSource
                .fetchBooks(keyword)
                .map { bookResponse: BookResponse -> bookResponse.toDomain() }
        return Books(value)
    }
}
