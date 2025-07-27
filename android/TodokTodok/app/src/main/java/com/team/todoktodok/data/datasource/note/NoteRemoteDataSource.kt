package com.team.todoktodok.data.datasource.note

import com.team.todoktodok.data.network.request.NoteRequest
import com.team.todoktodok.data.network.response.discussion.NoteResponse

interface NoteRemoteDataSource {
    suspend fun saveNote(request: NoteRequest)

    suspend fun fetchNotesByBookId(bookId: Long?): List<NoteResponse>
}
