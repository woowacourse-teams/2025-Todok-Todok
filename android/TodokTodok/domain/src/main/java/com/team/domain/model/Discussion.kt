package com.team.domain.model

import com.team.domain.model.member.User
import java.time.LocalDateTime

data class Discussion(
    val id: Long,
    val book: Book,
    val discussionTitle: String,
    val discussionOpinion: String,
    val writer: User,
    val createAt: LocalDateTime,
    val likeCount: Long,
    val commentCount: Long,
    val viewCount: Long,
    val isLikedByMe: Boolean,
) {
    val bookImage get() = book.image
    val writerNickname get() = writer.nickname.value

    fun getBookTitle(): String = book.extractSubtitle()

    fun getBookAuthor(): String = book.extractAuthor()

    fun modifyWriterProfileImage(profileImage: String): Discussion = copy(writer = writer.copy(profileImage = profileImage))
}
