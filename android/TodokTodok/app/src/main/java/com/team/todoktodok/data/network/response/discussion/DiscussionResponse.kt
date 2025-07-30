package com.team.todoktodok.data.network.response.discussion

import com.team.domain.model.Discussion
import com.team.todoktodok.data.core.ext.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscussionResponse(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("book")
    val bookResponse: BookResponse,
    @SerialName("discussionId")
    val discussionId: Long,
    @SerialName("discussionOpinion")
    val discussionOpinion: String,
    @SerialName("discussionTitle")
    val discussionTitle: String,
    @SerialName("member")
    val memberResponse: MemberResponse,
    @SerialName("note")
    val noteResponse: NoteResponse?,
)

fun DiscussionResponse.toDomain() =
    Discussion(
        id = discussionId,
        discussionTitle = discussionTitle,
        book = bookResponse.toDomain(),
        writer = memberResponse.toDomain(),
        createAt = createdAt.toLocalDateTime(),
        snap = noteResponse?.snap,
        discussionOpinion = discussionOpinion,
    )
