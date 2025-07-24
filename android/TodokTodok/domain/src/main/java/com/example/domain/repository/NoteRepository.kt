package com.example.domain.repository

interface NoteRepository {
    suspend fun saveNote(
        bookId: Long,
        snap: String,
        memo: String,
    )
}
