package com.team.todoktodok.data.network.response.latest

import com.team.domain.model.latest.LatestDiscussion
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.todoktodok.data.core.ext.toLocalDateTime
import com.team.todoktodok.data.network.response.discussion.toDomain
import kotlinx.serialization.Serializable

@Serializable
data class LatestDiscussionsResponse(
    val items: List<LatestDiscussionResponse>,
    val pageInfo: PageInfoResponse,
) {
    fun toDomain(): LatestDiscussionPage {
        val discussion =
            items.map {
                LatestDiscussion(
                    author = it.member.toDomain(),
                    book = it.book.toDomain(),
                    commentCount = it.commentCount,
                    content = it.discussionOpinion,
                    createdAt = it.createdAt.toLocalDateTime(),
                    discussionId = it.discussionId,
                    isLikedByMe = it.isLikedByMe,
                    likeCount = it.likeCount,
                    title = it.discussionTitle,
                )
            }

        return LatestDiscussionPage(
            discussions = discussion,
            pageInfo = pageInfo.toDomain(),
        )
    }
}
