package com.team.domain.model.book

data class BookDetail(
    val id: Long,
    val title: String,
    val author: String,
    val image: String,
    val publisher: String,
    val summary: String,
)
