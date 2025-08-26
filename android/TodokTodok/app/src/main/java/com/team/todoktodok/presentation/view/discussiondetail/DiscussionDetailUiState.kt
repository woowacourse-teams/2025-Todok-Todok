package com.team.todoktodok.presentation.view.discussiondetail

import com.team.domain.model.Discussion

data class DiscussionDetailUiState(
    val discussion: Discussion,
    val isMyDiscussion: Boolean,
    val isLoading: Boolean = false,
)
