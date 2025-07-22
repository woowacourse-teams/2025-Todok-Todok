package com.example.todoktodok.data.datasource

import com.example.todoktodok.data.network.request.NoteRequest

class RemoteNoteDataSource : NoteDataSource {
    override suspend fun saveNote(request: NoteRequest) {
    }
}
