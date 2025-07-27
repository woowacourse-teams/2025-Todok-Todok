package com.team.domain.model

import com.team.domain.model.member.User
import java.time.LocalDateTime

data class Discussion(
    val id: Long,
    val discussionTitle: String,
    val book: Book,
    val writer: User,
    val createAt: LocalDateTime,
    val snap: String,
    val discussionOpinion: String,
)
