package com.team.data.datasource.book

import com.team.data.network.request.BookRequest
import com.team.data.network.response.book.SearchedBookResultResponse
import com.team.data.network.response.discussion.BookDetailResponse
import com.team.data.network.response.discussion.page.BookDiscussionPageResponse
import com.team.domain.model.exception.NetworkResult

interface BookRemoteDataSource {
    suspend fun fetchBooks(
        size: Int,
        cursor: String?,
        keyword: String,
    ): NetworkResult<SearchedBookResultResponse>

    suspend fun fetchBook(bookId: Long): NetworkResult<BookDetailResponse>

    suspend fun fetchBookDiscussions(
        bookId: Long,
        size: Int,
        cursor: String?,
    ): NetworkResult<BookDiscussionPageResponse>

    suspend fun saveBook(bookRequest: BookRequest): NetworkResult<Long>
}
