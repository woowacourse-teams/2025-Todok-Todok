package com.team.data.network.service

import com.team.data.network.request.BookRequest
import com.team.data.network.response.book.SearchedBookResultResponse
import com.team.data.network.response.discussion.BookDetailResponse
import com.team.data.network.response.discussion.page.BookDiscussionPageResponse
import com.team.domain.model.exception.NetworkResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BookService {
    @GET("v1/books/searchByPaging")
    suspend fun fetchBooks(
        @Query("size") size: Int,
        @Query("cursor") cursor: String?,
        @Query("keyword") keyword: String,
    ): NetworkResult<SearchedBookResultResponse>

    @GET("v1/books/{bookId}")
    suspend fun fetchBook(
        @Path("bookId") bookId: Long,
    ): NetworkResult<BookDetailResponse>

    @GET("v1/books/{bookId}/discussions")
    suspend fun fetchBookDiscussions(
        @Path("bookId") bookId: Long,
        @Query("size") size: Int,
        @Query("cursor") cursor: String?,
    ): NetworkResult<BookDiscussionPageResponse>

    @POST("v1/books")
    suspend fun saveBook(
        @Body bookRequest: BookRequest,
    ): NetworkResult<Long>
}
