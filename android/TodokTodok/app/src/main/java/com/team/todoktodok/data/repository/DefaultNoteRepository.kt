package com.team.todoktodok.data.repository

import com.team.domain.model.Note
import com.team.domain.repository.NoteRepository
import com.team.todoktodok.data.datasource.note.NoteRemoteDataSource
import com.team.todoktodok.data.network.request.NoteRequest
import com.team.todoktodok.data.network.response.discussion.toDomain

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
