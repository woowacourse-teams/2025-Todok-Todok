package com.team.domain.repository

import com.team.domain.model.Note

interface NoteRepository {
    suspend fun saveNote(
        bookId: Long,
        snap: String,
        memo: String,
    )

    suspend fun fetchNotesByBookId(bookId: Long?): List<Note>
}
