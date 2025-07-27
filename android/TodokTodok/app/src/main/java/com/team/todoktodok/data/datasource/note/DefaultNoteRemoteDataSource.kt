package com.team.todoktodok.data.datasource.note

import com.team.todoktodok.data.network.request.NoteRequest
import com.team.todoktodok.data.network.service.NoteService

class DefaultNoteRemoteDataSource(
    private val noteService: NoteService,
) : NoteRemoteDataSource {
    override suspend fun saveNote(request: NoteRequest) {
        noteService.saveNote(request)
    }

    override suspend fun fetchNotesByBookId(bookId: Long?) = noteService.fetchNotesByBookId(bookId)
}
