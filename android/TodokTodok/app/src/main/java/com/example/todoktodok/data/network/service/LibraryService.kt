package com.example.todoktodok.data.network.service

import com.example.todoktodok.data.network.request.SaveBookRequest
import com.example.todoktodok.data.network.response.BookResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LibraryService {
    @GET("shelves")
    suspend fun fetchBooks(): List<BookResponse>

    @POST("shelves")
    suspend fun saveBook(
        @Body requestBody: SaveBookRequest,
    )
}
