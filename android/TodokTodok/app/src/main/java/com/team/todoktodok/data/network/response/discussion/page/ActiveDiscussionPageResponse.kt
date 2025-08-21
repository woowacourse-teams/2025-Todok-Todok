package com.team.todoktodok.data.network.response.discussion.page

import com.team.domain.model.active.ActivatedDiscussion
import com.team.domain.model.active.ActivatedDiscussionPage
import com.team.todoktodok.data.network.response.latest.PageInfoResponse
import kotlinx.serialization.Serializable

@Serializable
data class ActiveDiscussionPageResponse(
    val activeDiscussions: List<ActiveDiscussion>,
    val pageInfo: PageInfoResponse,
) {
    fun toDomain(): ActivatedDiscussionPage {
        val activatedDiscussions: List<ActivatedDiscussion> = activeDiscussions.map { it.toDomain() }

        return ActivatedDiscussionPage(
            activatedDiscussions,
            pageInfo.toDomain(),
        )
    }
}
