package com.team.domain.model.member

import com.team.domain.model.Book

data class MemberDiscussion(
    val id: Long,
    val book: Book,
    val discussionTitle: String,
    val discussionOpinion: String,
)
