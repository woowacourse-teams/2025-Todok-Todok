package com.team.todoktodok.presentation.view.discussiondetail.model

import com.team.domain.model.Reply
import kotlinx.coroutines.Job

data class ReplyItemUiState(
    val reply: Reply,
    val isMyReply: Boolean,
    val itemJob: Job? = null,
)
