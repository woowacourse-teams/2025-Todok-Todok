package com.team.todoktodok.data.network.response.discussion.page

import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionPage
import com.team.todoktodok.data.network.response.latest.PageInfoResponse
import kotlinx.serialization.Serializable

@Serializable
data class ActiveDiscussionPageResponse(
    val items: List<ActiveDiscussion>,
    val pageInfo: PageInfoResponse,
) {
    fun toDomain(): DiscussionPage {
        val activatedDiscussions: List<Discussion> = items.map { it.toDomain() }

        return DiscussionPage(
            activatedDiscussions,
            pageInfo.toDomain(),
        )
    }
}
