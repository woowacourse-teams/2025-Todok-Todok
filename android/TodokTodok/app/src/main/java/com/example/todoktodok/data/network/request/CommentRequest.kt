package com.example.todoktodok.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class CommentRequest(
    val content: String,
)
