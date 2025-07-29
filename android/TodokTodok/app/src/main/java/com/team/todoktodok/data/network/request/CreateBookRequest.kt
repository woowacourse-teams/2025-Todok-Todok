package com.team.todoktodok.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateBookRequest(
    val bookIsbn: Long,
    val bookTitle: String,
    val bookAuthor: String,
    val bookImage: String,
)
