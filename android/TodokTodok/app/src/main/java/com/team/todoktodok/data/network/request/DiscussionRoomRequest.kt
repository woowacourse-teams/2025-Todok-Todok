package com.team.todoktodok.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class DiscussionRoomRequest(
    val bookId: Long,
    val discussionTitle: String,
    val discussionOpinion: String,
)