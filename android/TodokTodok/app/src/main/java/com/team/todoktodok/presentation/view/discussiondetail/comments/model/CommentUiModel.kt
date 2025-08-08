package com.team.todoktodok.presentation.view.discussiondetail.comments.model

import com.team.domain.model.Comment

data class CommentUiModel(
    val comment: Comment,
    val isMyComment: Boolean,
)
