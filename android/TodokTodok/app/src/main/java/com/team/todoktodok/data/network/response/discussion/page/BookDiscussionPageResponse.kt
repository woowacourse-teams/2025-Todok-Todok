package com.team.todoktodok.data.network.response.discussion.page

import com.team.domain.model.discussionroom.page.BookDiscussionsPage
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import com.team.todoktodok.data.network.response.discussion.toDomain
import com.team.todoktodok.data.network.response.latest.PageInfoResponse
import kotlinx.serialization.Serializable

@Serializable
data class BookDiscussionPageResponse(
    val discussionResponses: List<DiscussionResponse>,
    val pageInfoResponse: PageInfoResponse,
)

fun BookDiscussionPageResponse.toDomain() =
    BookDiscussionsPage(
        discussionResponses.map { it.toDomain() },
        pageInfoResponse.toDomain(),
    )
