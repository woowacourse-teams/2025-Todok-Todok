package com.example.todoktodok.data.network.service

import com.example.todoktodok.data.network.request.NoteRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NoteService {
    @POST("notes")
    suspend fun saveNote(
        @Body requestBody: NoteRequest,
    ): Response<Unit>
}
