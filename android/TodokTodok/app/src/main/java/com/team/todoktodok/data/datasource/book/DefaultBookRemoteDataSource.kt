package com.team.todoktodok.data.datasource.book

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.request.BookRequest
import com.team.todoktodok.data.network.response.book.SearchedBookResultResponse
import com.team.todoktodok.data.network.response.discussion.BookDetailResponse
import com.team.todoktodok.data.network.response.discussion.page.BookDiscussionPageResponse
import com.team.todoktodok.data.network.service.BookService

class DefaultBookRemoteDataSource(
    private val bookService: BookService,
) : BookRemoteDataSource {
    override suspend fun fetchBooks(
        size: Int,
        cursor: String?,
        keyword: String,
    ): NetworkResult<SearchedBookResultResponse> = bookService.fetchBooks(size, cursor, keyword)

    override suspend fun fetchBook(bookId: Long): NetworkResult<BookDetailResponse> = bookService.fetchBook(bookId)

    override suspend fun fetchBookDiscussions(
        bookId: Long,
        size: Int,
        cursor: String?,
    ): NetworkResult<BookDiscussionPageResponse> = bookService.fetchBookDiscussions(bookId, size, cursor)

    override suspend fun saveBook(bookRequest: BookRequest): NetworkResult<Long> = bookService.saveBook(bookRequest)
}
