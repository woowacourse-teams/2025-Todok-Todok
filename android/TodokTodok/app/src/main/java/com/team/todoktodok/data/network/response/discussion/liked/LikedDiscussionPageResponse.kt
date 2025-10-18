package com.team.todoktodok.data.network.response.discussion.liked

import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionPage
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import com.team.todoktodok.data.network.response.discussion.toDomain
import com.team.todoktodok.data.network.response.latest.PageInfoResponse
import kotlinx.serialization.Serializable

@Serializable
data class LikedDiscussionPageResponse(
    val items: List<DiscussionResponse>,
    val pageInfo: PageInfoResponse,
) {
    fun toDomain(): DiscussionPage {
        val discussions: List<Discussion> = items.map { it.toDomain() }

        return DiscussionPage(
            discussions = discussions,
            pageInfo = pageInfo.toDomain(),
        )
    }
}
