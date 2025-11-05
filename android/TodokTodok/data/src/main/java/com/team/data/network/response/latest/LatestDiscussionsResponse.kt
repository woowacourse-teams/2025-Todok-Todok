package com.team.data.network.response.latest

import com.team.data.core.ext.toLocalDateTime
import com.team.data.network.response.discussion.toDomain
import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionPage
import kotlinx.serialization.Serializable

@Serializable
data class LatestDiscussionsResponse(
    val items: List<LatestDiscussionResponse>,
    val pageInfo: PageInfoResponse,
) {
    fun toDomain(): DiscussionPage {
        val discussion =
            items.map {
                Discussion(
                    id = it.discussionId,
                    writer = it.member.toDomain(),
                    book = it.book.toDomain(),
                    commentCount = it.commentCount,
                    discussionOpinion = it.discussionOpinion,
                    discussionTitle = it.discussionTitle,
                    createAt = it.createdAt.toLocalDateTime(),
                    isLikedByMe = it.isLikedByMe,
                    likeCount = it.likeCount,
                    viewCount = it.viewCount,
                )
            }

        return DiscussionPage(
            discussions = discussion,
            pageInfo = pageInfo.toDomain(),
        )
    }
}
