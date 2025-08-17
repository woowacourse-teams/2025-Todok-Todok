package com.team.todoktodok.presentation.view.serialization

import android.os.Parcelable
import com.team.domain.model.member.MemberDiscussion
import kotlinx.parcelize.Parcelize

@Parcelize
class SerializationMemberDiscussion(
    val id: Long,
    val book: SerializationBook,
    val discussionOpinion: String,
    val discussionTitle: String,
) : Parcelable {
    fun toDomain(): MemberDiscussion =
        MemberDiscussion(
            id = id,
            book = book.toDomain(),
            discussionOpinion = discussionOpinion,
            discussionTitle = discussionTitle,
        )
}

fun MemberDiscussion.toSerialization(): SerializationMemberDiscussion =
    SerializationMemberDiscussion(
        id = id,
        book = book.toSerialization(),
        discussionOpinion = discussionOpinion,
        discussionTitle = discussionTitle,
    )
