package com.example.todoktodok.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class DiscussionRequest(
    val discussionOpinion: String,
    val discussionTitle: String,
    val noteId: Long,
)
