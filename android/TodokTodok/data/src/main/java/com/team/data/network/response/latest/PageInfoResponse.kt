package com.team.data.network.response.latest

import com.team.domain.model.PageInfo
import kotlinx.serialization.Serializable

@Serializable
data class PageInfoResponse(
    val hasNext: Boolean,
    val nextCursor: String?,
) {
    fun toDomain(): PageInfo = PageInfo(hasNext, nextCursor)
}
