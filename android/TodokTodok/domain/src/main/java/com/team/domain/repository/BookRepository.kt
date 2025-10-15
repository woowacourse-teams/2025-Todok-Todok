package com.team.domain.repository

import com.team.domain.model.Book
import com.team.domain.model.book.Keyword
import com.team.domain.model.book.SearchedBooksResult
import com.team.domain.model.discussionroom.page.BookDiscussionsPage
import com.team.domain.model.exception.NetworkResult

interface BookRepository {
    suspend fun fetchBooks(
        size: Int,
        keyword: Keyword,
    ): NetworkResult<SearchedBooksResult>

    suspend fun fetchBook(bookId: Long): NetworkResult<Book>

    suspend fun getBookDiscussions(
        bookId: Long,
        size: Int,
        cursor: String?,
    ): NetworkResult<BookDiscussionsPage>

    suspend fun saveBook(book: Book): NetworkResult<Long>
}
