package com.example.todoktodok.fake.datasource

import com.example.todoktodok.data.datasource.note.NoteRemoteDataSource
import com.example.todoktodok.data.network.request.NoteRequest
import com.example.todoktodok.data.network.response.discussion.NoteResponse

class FakeNoteDataSource : NoteRemoteDataSource {
    val savedRequests = mutableListOf<NoteRequest>()

    override suspend fun saveNote(request: NoteRequest) {
        savedRequests.add(request)
    }

    override suspend fun fetchNotesByBookId(bookId: Long?): List<NoteResponse> {
        TODO("Not yet implemented")
    }
}
