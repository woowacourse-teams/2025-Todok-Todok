package com.team.todoktodok.data.network.service

import com.team.todoktodok.data.network.request.NoteRequest
import com.team.todoktodok.data.network.response.discussion.NoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NoteService {
    @POST("notes")
    suspend fun saveNote(
        @Body requestBody: NoteRequest,
    ): Response<Unit>

    @GET("notes/mine")
    suspend fun fetchNotesByBookId(
        @Query("bookId") bookId: Long?,
    ): List<NoteResponse>
}
