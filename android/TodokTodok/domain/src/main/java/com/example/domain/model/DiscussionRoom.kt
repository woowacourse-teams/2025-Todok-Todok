package com.example.domain.model

import com.example.domain.model.member.User
import java.time.LocalDateTime

data class DiscussionRoom(
    val id: Long,
    val discussionTitle: String,
    val book: Book,
    val writer: User,
    val createAt: LocalDateTime,
    val snap: String,
    val discussionOpinion: String,
)
