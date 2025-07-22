package com.example.domain.repository

import com.example.domain.model.Note

interface NoteRepository {
    suspend fun saveNote(note: Note)
}
