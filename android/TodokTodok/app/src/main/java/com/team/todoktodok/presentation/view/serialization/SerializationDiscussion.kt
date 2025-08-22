package com.team.todoktodok.presentation.view.serialization

import android.os.Parcelable
import com.team.domain.model.Discussion
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
class SerializationDiscussion(
    val id: Long,
    val book: SerializationBook,
    val discussionTitle: String,
    val discussionOpinion: String,
    val writer: SerializationUser,
    val createAt: LocalDateTime,
    val likeCount: Int,
    val commentCount: Int,
    val viewCount: Int = 0,
    val isLikedByMe: Boolean,
) : Parcelable {
    fun toDomain(): Discussion =
        Discussion(
            id = id,
            book = book.toDomain(),
            discussionOpinion = discussionOpinion,
            discussionTitle = discussionTitle,
            writer = writer.toDomain(),
            createAt = createAt,
            likeCount = likeCount,
            commentCount = commentCount,
            viewCount = viewCount,
            isLikedByMe = isLikedByMe,
        )
}

fun Discussion.toSerialization(): SerializationDiscussion =
    SerializationDiscussion(
        id = id,
        book = book.toSerialization(),
        discussionOpinion = discussionOpinion,
        discussionTitle = discussionTitle,
        writer = writer.toSerialization(),
        createAt = createAt,
        likeCount = likeCount,
        commentCount = commentCount,
        viewCount = viewCount,
        isLikedByMe = isLikedByMe,
    )
