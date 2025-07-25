package com.example.todoktodok.data.datasource.note

import com.example.todoktodok.data.network.request.NoteRequest
import com.example.todoktodok.data.network.service.NoteService

class DefaultNoteRemoteDataSource(
    private val noteService: NoteService,
) : NoteRemoteDataSource {
    override suspend fun saveNote(request: NoteRequest) {
        noteService.saveNote(request)
    }

    override suspend fun fetchNotesByBookId(bookId: Long?) = noteService.fetchNotesByBookId(bookId)
}
