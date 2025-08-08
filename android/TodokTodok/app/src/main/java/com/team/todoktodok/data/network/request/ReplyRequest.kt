package com.team.todoktodok.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class ReplyRequest(
    val content: String,
)
