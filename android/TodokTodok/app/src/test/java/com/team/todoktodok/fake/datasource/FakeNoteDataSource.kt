package com.team.todoktodok.fake.datasource

import com.team.todoktodok.data.datasource.note.NoteRemoteDataSource
import com.team.todoktodok.data.network.request.NoteRequest
import com.team.todoktodok.data.network.response.discussion.NoteResponse

class FakeNoteDataSource : NoteRemoteDataSource {
    val savedRequests = mutableListOf<NoteRequest>()

    override suspend fun saveNote(request: NoteRequest) {
        savedRequests.add(request)
    }

    override suspend fun fetchNotesByBookId(bookId: Long?): List<NoteResponse> {
        TODO("Not yet implemented")
    }
}
