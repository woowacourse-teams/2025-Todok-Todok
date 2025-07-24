package com.example.todoktodok.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscussionRequest(
    @SerialName("discussionOpinion")
    val discussionOpinion: String,
    @SerialName("discussionTitle")
    val discussionTitle: String,
    @SerialName("noteId")
    val noteId: Long,
)
