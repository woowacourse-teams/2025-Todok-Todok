package com.team.data.network.response.discussion.page

import com.team.data.network.response.discussion.DiscussionResponse
import com.team.data.network.response.discussion.toDomain
import com.team.data.network.response.latest.PageInfoResponse
import com.team.domain.model.discussionroom.page.BookDiscussionsPage
import kotlinx.serialization.Serializable

@Serializable
data class BookDiscussionPageResponse(
    val items: List<DiscussionResponse>,
    val pageInfo: PageInfoResponse,
)

fun BookDiscussionPageResponse.toDomain() =
    BookDiscussionsPage(
        items.map { it.toDomain() },
        pageInfo.toDomain(),
    )
