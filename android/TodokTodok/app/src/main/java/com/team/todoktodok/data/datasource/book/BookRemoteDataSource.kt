package com.team.todoktodok.data.datasource.book

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.request.BookRequest
import com.team.todoktodok.data.network.response.book.SearchedBookResultResponse
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.response.discussion.page.BookDiscussionPageResponse

interface BookRemoteDataSource {
    suspend fun fetchBooks(
        size: Int,
        cursor: String?,
        keyword: String,
    ): NetworkResult<SearchedBookResultResponse>

    suspend fun fetchBook(bookId: Long): NetworkResult<BookResponse>

    suspend fun fetchBookDiscussions(
        bookId: Long,
        size: Int,
        cursor: String?,
    ): NetworkResult<BookDiscussionPageResponse>

    suspend fun saveBook(bookRequest: BookRequest): NetworkResult<Long>
}
