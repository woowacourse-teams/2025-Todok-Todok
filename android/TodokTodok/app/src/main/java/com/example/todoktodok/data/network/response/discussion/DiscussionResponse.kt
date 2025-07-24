package com.example.todoktodok.data.network.response.discussion

import com.example.domain.model.Discussion
import com.example.todoktodok.data.core.ext.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscussionResponse(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("bookResponse")
    val bookResponse: BookResponse,
    @SerialName("discussionId")
    val discussionId: Long,
    @SerialName("discussionOpinion")
    val discussionOpinion: String,
    @SerialName("discussionTitle")
    val discussionTitle: String,
    @SerialName("memberResponse")
    val memberResponse: MemberResponse,
    @SerialName("noteResponse")
    val noteResponse: NoteResponse,
)

fun DiscussionResponse.toDomain() =
    Discussion(
        id = discussionId,
        discussionTitle = discussionTitle,
        book = noteResponse.book.toDomain(),
        writer = memberResponse.toDomain(),
        createAt = createdAt.toLocalDateTime(),
        snap = noteResponse.snap,
        discussionOpinion = discussionOpinion,
    )
