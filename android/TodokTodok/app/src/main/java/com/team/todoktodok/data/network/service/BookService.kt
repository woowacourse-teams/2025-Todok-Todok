package com.team.todoktodok.data.network.service

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.request.BookRequest
import com.team.todoktodok.data.network.response.book.SearchedBookResultResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BookService {
    @GET("v1/books/searchByPaging")
    suspend fun fetchBooks(
        @Query("size") size: Int,
        @Query("cursor") cursor: String?,
        @Query("keyword") keyword: String,
    ): NetworkResult<SearchedBookResultResponse>

    @POST("v1/books")
    suspend fun saveBook(
        @Body bookRequest: BookRequest,
    ): NetworkResult<Long>
}
