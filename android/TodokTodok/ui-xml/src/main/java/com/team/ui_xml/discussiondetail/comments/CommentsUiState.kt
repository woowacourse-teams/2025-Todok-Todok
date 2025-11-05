package com.team.ui_xml.discussiondetail.comments

import com.team.ui_xml.discussiondetail.model.CommentItemUiState

data class CommentsUiState(
    val comments: List<CommentItemUiState> = emptyList(),
    val commentContent: String = "",
    val isLoading: Boolean = false,
)
