package com.team.todoktodok.data.network.response.discussion

import com.team.domain.model.member.MemberDiscussion
import kotlinx.serialization.Serializable

@Serializable
data class MemberDiscussionResponse(
    val book: BookResponse,
    val discussionId: Long,
    val discussionOpinion: String,
    val discussionTitle: String,
) {
    fun toDomain(): MemberDiscussion =
        MemberDiscussion(
            id = discussionId,
            book = book.toDomain(),
            discussionOpinion = discussionOpinion,
            discussionTitle = discussionTitle,
        )
}
