package com.team.data.network.response.discussion.page

import com.team.data.network.response.latest.PageInfoResponse
import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionPage
import kotlinx.serialization.Serializable

@Serializable
data class ActivatedDiscussionPageResponse(
    val items: List<ActivatedDiscussion>,
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
