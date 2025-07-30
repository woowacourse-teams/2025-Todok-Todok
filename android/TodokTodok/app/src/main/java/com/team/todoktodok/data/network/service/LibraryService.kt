package com.team.todoktodok.data.network.service

import com.team.todoktodok.data.network.request.CreateBookRequest
import com.team.todoktodok.data.network.request.SaveBookRequest
import com.team.todoktodok.data.network.response.BookResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LibraryService {
    @GET("v1/shelves")
    suspend fun fetchBooks(): List<BookResponse>

    @POST("v1/shelves")
    suspend fun saveBook(
        @Body requestBody: SaveBookRequest,
    )

    @POST("v2/books")
    suspend fun createBook(
        @Body requestBody: CreateBookRequest,
    ): Response<Long>
}
