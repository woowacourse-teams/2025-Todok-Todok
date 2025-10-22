package com.team.domain.repository

import com.team.domain.model.book.BookDetail
import com.team.domain.model.book.Keyword
import com.team.domain.model.book.SearchedBook
import com.team.domain.model.book.SearchedBooksResult
import com.team.domain.model.discussionroom.page.BookDiscussionsPage
import com.team.domain.model.exception.NetworkResult

interface BookRepository {
    fun deleteCursor()

    suspend fun fetchBooks(
        size: Int,
        keyword: Keyword,
    ): NetworkResult<SearchedBooksResult>

    suspend fun getBookDetail(bookId: Long): NetworkResult<BookDetail>

    suspend fun getBookDiscussions(
        bookId: Long,
        size: Int,
        cursor: String?,
    ): NetworkResult<BookDiscussionsPage>

    suspend fun saveBook(book: SearchedBook): NetworkResult<Long>
}
