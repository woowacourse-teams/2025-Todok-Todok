package com.example.todoktodok.data.repository

import com.example.domain.repository.NoteRepository
import com.example.todoktodok.data.datasource.note.NoteDataSource
import com.example.todoktodok.data.network.request.NoteRequest

class DefaultNoteRepository(
    private val remoteNoteDataSource: NoteDataSource,
) : NoteRepository {
    override suspend fun saveNote(
        bookId: Long,
        snap: String,
        memo: String,
    ) {
        val request = NoteRequest(bookId, snap, memo)
        remoteNoteDataSource.saveNote(request)
    }
}
