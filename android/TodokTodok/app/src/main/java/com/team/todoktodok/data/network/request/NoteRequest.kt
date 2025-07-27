package com.team.todoktodok.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest(
    val bookId: Long,
    val snap: String,
    val memo: String,
)
