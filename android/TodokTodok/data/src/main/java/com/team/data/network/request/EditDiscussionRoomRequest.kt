package com.team.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class EditDiscussionRoomRequest(
    val discussionTitle: String,
    val discussionOpinion: String,
)
