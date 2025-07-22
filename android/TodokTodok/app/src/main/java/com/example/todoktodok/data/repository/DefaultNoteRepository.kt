package com.example.todoktodok.data.repository

import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import com.example.todoktodok.data.datasource.NoteDataSource
import com.example.todoktodok.data.network.request.toRequest

class DefaultNoteRepository(
    private val remoteNoteDataSource: NoteDataSource,
) : NoteRepository {
    override suspend fun saveNote(note: Note) {
        remoteNoteDataSource.saveNote(note.toRequest())
    }
}
