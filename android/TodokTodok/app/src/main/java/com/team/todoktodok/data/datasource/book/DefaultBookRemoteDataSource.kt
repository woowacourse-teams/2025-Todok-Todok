package com.team.todoktodok.data.datasource.book

import com.team.domain.model.Book
import com.team.domain.model.Books
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.response.discussion.toDomain
import com.team.todoktodok.data.network.service.BookService

class DefaultBookRemoteDataSource(
    private val bookService: BookService,
) : BookRemoteDataSource {
    override suspend fun fetchBooks(keyword: String): Books {
        val value: List<Book> =
            bookService
                .fetchBooks(keyword)
                .map { bookResponse: BookResponse -> bookResponse.toDomain() }
        return Books(value)
    }
}
