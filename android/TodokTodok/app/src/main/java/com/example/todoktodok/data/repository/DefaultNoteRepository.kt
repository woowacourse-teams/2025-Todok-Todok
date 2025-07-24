package com.example.todoktodok.data.repository

import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import com.example.todoktodok.data.datasource.note.NoteDataSource
import com.example.todoktodok.data.network.request.NoteRequest
import com.example.todoktodok.data.network.response.discussion.toDomain

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

    override suspend fun fetchNotesByBookId(bookId: Long?): List<Note> =
        remoteNoteDataSource.fetchNotesByBookId(bookId).map { it.toDomain() }
}
