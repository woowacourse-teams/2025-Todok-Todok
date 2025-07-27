package com.team.todoktodok.data.network.service

import com.team.todoktodok.data.network.response.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("books/search")
    suspend fun fetchBooks(
        @Query("keyword") searchInput: String,
    ): List<BookResponse>
}
