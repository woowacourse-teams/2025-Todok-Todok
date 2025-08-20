package com.team.todoktodok.data.network.response.latest

import com.team.domain.model.latest.LatestDiscussion
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.todoktodok.data.core.ext.toLocalDateTime
import com.team.todoktodok.data.network.response.discussion.toDomain
import kotlinx.serialization.Serializable

@Serializable
data class LatestDiscussionsResponse(
    val latestDiscussionResponses: List<LatestDiscussionResponse>,
    val pageInfoResponse: PageInfoResponse,
) {
    fun toDomain(): LatestDiscussionPage {
        val discussion =
            latestDiscussionResponses.map {
                LatestDiscussion(
                    authorResponse = it.author.toDomain(),
                    book = it.book.toDomain(),
                    commentCount = it.commentCount,
                    content = it.content,
                    createdAt = it.createdAt.toLocalDateTime(),
                    discussionId = it.discussionId,
                    isLikedByMe = it.isLikedByMe,
                    likeCount = it.likeCount,
                    title = it.title,
                )
            }

        return LatestDiscussionPage(
            discussions = discussion,
            pageInfo = pageInfoResponse.toDomain(),
        )
    }
}
