package com.team.data.datasource.book

import com.team.data.network.request.BookRequest
import com.team.data.network.response.book.SearchedBookResultResponse
import com.team.data.network.response.discussion.BookDetailResponse
import com.team.data.network.response.discussion.page.BookDiscussionPageResponse
import com.team.data.network.service.BookService
import com.team.domain.model.exception.NetworkResult
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

        override suspend fun fetchBook(bookId: Long): NetworkResult<BookDetailResponse> = bookService.fetchBook(bookId)

        override suspend fun fetchBookDiscussions(
            bookId: Long,
            size: Int,
            cursor: String?,
        ): NetworkResult<BookDiscussionPageResponse> = bookService.fetchBookDiscussions(bookId, size, cursor)
    }
