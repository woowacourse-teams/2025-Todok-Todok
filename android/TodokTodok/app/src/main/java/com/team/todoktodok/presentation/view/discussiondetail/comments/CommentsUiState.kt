package com.team.todoktodok.presentation.view.discussiondetail.comments

import com.team.todoktodok.presentation.view.discussiondetail.model.CommentItemUiState

data class CommentsUiState(
    val comments: List<CommentItemUiState> = emptyList(),
    val commentContent: String = "",
    val isLoading: Boolean = false,
)
