package com.example.domain.repository

import com.example.domain.model.Note

interface NoteRepository {
    suspend fun saveNote(
        bookId: Long,
        snap: String,
        memo: String,
    )

    suspend fun fetchNotesByBookId(bookId: Long?): List<Note>
}
