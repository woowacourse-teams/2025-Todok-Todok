package com.team.todoktodok.data.datasource.book

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.request.BookRequest
import com.team.todoktodok.data.network.response.book.AladinBookResponse
import com.team.todoktodok.data.network.service.BookService

class DefaultBookRemoteDataSource(
    private val bookService: BookService,
) : BookRemoteDataSource {
    override suspend fun fetchBooks(keyword: String): NetworkResult<List<AladinBookResponse>> = bookService.fetchBooks(keyword)

    override suspend fun saveBook(bookRequest: BookRequest): NetworkResult<Long> = bookService.saveBook(bookRequest)
}
