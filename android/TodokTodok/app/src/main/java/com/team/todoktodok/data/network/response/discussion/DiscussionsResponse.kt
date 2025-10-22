package com.team.todoktodok.data.network.response.discussion

import kotlinx.serialization.Serializable

@Serializable
data class DiscussionsResponse(
    val items: List<DiscussionResponse>,
)
