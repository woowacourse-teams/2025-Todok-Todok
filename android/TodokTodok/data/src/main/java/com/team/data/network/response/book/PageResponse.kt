package com.team.data.network.response.book

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageResponse(
    @SerialName("hasNext")
    val hasNext: Boolean,
    @SerialName("nextCursor")
    val nextCursor: String?,
    @SerialName("currentSize")
    val currentSize: Int,
)
