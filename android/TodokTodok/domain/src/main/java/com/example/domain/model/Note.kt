package com.example.domain.model

data class Note(
    val id: Long,
    val snap: String,
    val memo: String,
    val book: Book,
)
