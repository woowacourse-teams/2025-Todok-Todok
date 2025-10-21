package com.team.todoktodok.data.datasource.book

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.request.BookRequest
import com.team.todoktodok.data.network.response.book.SearchedBookResultResponse
import com.team.todoktodok.data.network.service.BookService
import javax.inject.Inject

class DefaultBookRemoteDataSource
    @Inject
    constructor(
        private val bookService: BookService,
    ) : BookRemoteDataSource {
        override suspend fun fetchBooks(
            size: Int,
            cursor: String?,
            keyword: String,
        ): NetworkResult<SearchedBookResultResponse> = bookService.fetchBooks(size, cursor, keyword)

        override suspend fun saveBook(bookRequest: BookRequest): NetworkResult<Long> = bookService.saveBook(bookRequest)
    }
