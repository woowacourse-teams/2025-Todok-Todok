package com.team.todoktodok.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class DiscussionRequest(
    val discussionTitle: String,
    val discussionOpinion: String,
    val noteId: Long,
)
