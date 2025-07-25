package com.example.todoktodok.data.datasource.note

import com.example.todoktodok.data.network.request.NoteRequest
import com.example.todoktodok.data.network.response.discussion.NoteResponse

interface NoteRemoteDataSource {
    suspend fun saveNote(request: NoteRequest)

    suspend fun fetchNotesByBookId(bookId: Long?): List<NoteResponse>
}
