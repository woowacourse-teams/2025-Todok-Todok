package com.team.todoktodok.data.datasource.book

import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.service.BookService

class DefaultBookRemoteDataSource(
    private val bookService: BookService,
) : BookRemoteDataSource {
    override suspend fun fetchBooks(keyword: String): List<BookResponse> = bookService.fetchBooks(keyword)
}
