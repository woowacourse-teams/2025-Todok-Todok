package com.team.todoktodok.presentation.view.discussiondetail.model

import com.team.domain.model.Comment
import kotlinx.coroutines.Job

data class CommentItemUiState(
    val comment: Comment,
    val isMyComment: Boolean,
    val itemJob: Job? = null,
)
