package com.team.todoktodok.data.network.response.discussion.page

import com.team.domain.model.Discussion
import com.team.domain.model.active.ActivatedDiscussionPage
import com.team.todoktodok.data.network.response.latest.PageInfoResponse
import kotlinx.serialization.Serializable

@Serializable
data class ActiveDiscussionPageResponse(
    val items: List<ActiveDiscussion>,
    val pageInfo: PageInfoResponse,
) {
    fun toDomain(): ActivatedDiscussionPage {
        val activatedDiscussions: List<Discussion> = items.map { it.toDomain() }

        return ActivatedDiscussionPage(
            activatedDiscussions,
            pageInfo.toDomain(),
        )
    }
}
