package com.team.todoktodok.data.repository

import com.team.domain.model.book.BookDetail
import com.team.domain.model.book.Keyword
import com.team.domain.model.book.SearchedBook
import com.team.domain.model.book.SearchedBooksResult
import com.team.domain.model.discussionroom.page.BookDiscussionsPage
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.map
import com.team.domain.repository.BookRepository
import com.team.todoktodok.data.datasource.book.BookRemoteDataSource
import com.team.todoktodok.data.network.request.toRequest
import com.team.todoktodok.data.network.response.book.toDomain
import com.team.todoktodok.data.network.response.discussion.page.toDomain
import com.team.todoktodok.data.network.response.discussion.toDomain
import javax.inject.Inject

class DefaultBookRepository
    @Inject
    constructor(
        private val bookRemoteDataSource: BookRemoteDataSource,
    ) : BookRepository {
        private var cursor: String? = null
        private var keyword: Keyword? = null

        override suspend fun fetchBooks(
            size: Int,
            keyword: Keyword,
        ): NetworkResult<SearchedBooksResult> {
            if (this.keyword != keyword) {
                cursor = null
            }

            this.keyword = keyword

            val result =
                bookRemoteDataSource
                    .fetchBooks(size, cursor, keyword.value)

            result.map { response ->
                this.cursor = response.pageInfo.nextCursor
            }

            return result.map { response ->
                response.toDomain()
            }
        }

        override fun deleteCursor() {
            cursor = null
        }

        override suspend fun saveBook(book: SearchedBook): NetworkResult<Long> = bookRemoteDataSource.saveBook(book.toRequest())

        override suspend fun getBookDetail(bookId: Long): NetworkResult<BookDetail> =
            bookRemoteDataSource.fetchBook(bookId).map { it.toDomain() }

        override suspend fun getBookDiscussions(
            bookId: Long,
            size: Int,
            cursor: String?,
        ): NetworkResult<BookDiscussionsPage> =
            bookRemoteDataSource
                .fetchBookDiscussions(bookId, size, cursor)
                .map { discussions -> discussions.toDomain() }
    }
