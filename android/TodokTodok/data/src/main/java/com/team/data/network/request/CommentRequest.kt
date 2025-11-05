package com.team.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class CommentRequest(
    val content: String,
)
