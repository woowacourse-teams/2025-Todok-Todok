package com.team.todoktodok.presentation.view.discussiondetail.model

import com.team.domain.model.Reply

data class ReplyUiModel(
    val reply: Reply,
    val isMyReply: Boolean,
)
