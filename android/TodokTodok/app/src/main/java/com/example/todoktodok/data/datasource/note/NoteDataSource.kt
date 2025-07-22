package com.example.todoktodok.data.datasource.note

import com.example.todoktodok.data.network.request.NoteRequest

interface NoteDataSource {
    suspend fun saveNote(request: NoteRequest)
}
