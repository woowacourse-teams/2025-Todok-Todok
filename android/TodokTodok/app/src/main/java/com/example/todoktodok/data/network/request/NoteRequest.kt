package com.example.todoktodok.data.network.request

data class NoteRequest(
    val bookId: Long,
    val snap: String,
    val memo: String,
)
