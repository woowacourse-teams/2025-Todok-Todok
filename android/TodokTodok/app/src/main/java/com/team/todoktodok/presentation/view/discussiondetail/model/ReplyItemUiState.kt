package com.team.todoktodok.presentation.view.discussiondetail.model

import com.team.domain.model.Reply

data class ReplyItemUiState(
    val reply: Reply,
    val isMyReply: Boolean,
)
