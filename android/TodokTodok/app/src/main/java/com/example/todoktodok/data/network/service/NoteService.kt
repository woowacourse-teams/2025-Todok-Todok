package com.example.todoktodok.data.network.service

import com.example.todoktodok.data.network.request.NoteRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface NoteService {
    @POST("/api/v1/notes")
    suspend fun saveNote(
        @Header(
            "Authorization",
        ) token: String = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3NTMyNjA1MDd9.rT_yrb-NIWv127OrwnElkAy9vAgJKWbgxY6gdhwfnUE",
        @Body requestBody: NoteRequest,
    ): Response<Unit>
}
