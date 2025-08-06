package com.team.todoktodok.data.network.service

import com.team.todoktodok.data.network.response.discussion.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("v1/books/search")
    suspend fun fetchBooks(
        @Query("keyword") keyword: String,
    ): List<BookResponse>
}
