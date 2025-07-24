package com.example.todoktodok.data.network.service

import com.example.todoktodok.data.network.response.BookResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BookService {
    @GET("books/search")
    suspend fun fetchBooks(
        @Header("Authorization") accessToken: String,
        @Query("keyword") searchInput: String,
    ): List<BookResponse>
}
