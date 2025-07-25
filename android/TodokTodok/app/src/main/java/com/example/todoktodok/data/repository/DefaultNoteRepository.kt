package com.example.todoktodok.data.repository

import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import com.example.todoktodok.data.datasource.note.NoteRemoteDataSource
import com.example.todoktodok.data.network.request.NoteRequest
import com.example.todoktodok.data.network.response.discussion.toDomain

class DefaultNoteRepository(
    private val remoteNoteRemoteDataSource: NoteRemoteDataSource,
) : NoteRepository {
    override suspend fun saveNote(
        bookId: Long,
        snap: String,
        memo: String,
    ) {
        val request = NoteRequest(bookId, snap, memo)
        remoteNoteRemoteDataSource.saveNote(request)
    }

    override suspend fun fetchNotesByBookId(bookId: Long?): List<Note> =
        remoteNoteRemoteDataSource.fetchNotesByBookId(bookId).map { it.toDomain() }
}
