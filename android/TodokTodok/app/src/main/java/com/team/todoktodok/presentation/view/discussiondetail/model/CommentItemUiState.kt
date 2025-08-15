package com.team.todoktodok.presentation.view.discussiondetail.model

import com.team.domain.model.Comment

data class CommentItemUiState(
    val comment: Comment,
    val isMyComment: Boolean,
)
