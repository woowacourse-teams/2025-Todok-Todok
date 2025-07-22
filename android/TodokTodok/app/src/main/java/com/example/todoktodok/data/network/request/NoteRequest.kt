package com.example.todoktodok.data.network.request

import com.example.domain.model.Note

data class NoteRequest(
    val bookId: Long,
    val snap: String,
    val memo: String,
)

fun Note.toRequest(): NoteRequest =
    NoteRequest(
        bookId = this.book.id,
        snap = this.snap,
        memo = this.memo,
    )
