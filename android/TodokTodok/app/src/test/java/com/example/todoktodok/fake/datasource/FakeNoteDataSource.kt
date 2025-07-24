package com.example.todoktodok.fake.datasource

import com.example.todoktodok.data.datasource.note.NoteDataSource
import com.example.todoktodok.data.network.request.NoteRequest

class FakeNoteDataSource : NoteDataSource {
    val savedRequests = mutableListOf<NoteRequest>()

    override suspend fun saveNote(request: NoteRequest) {
        savedRequests.add(request)
    }
}
