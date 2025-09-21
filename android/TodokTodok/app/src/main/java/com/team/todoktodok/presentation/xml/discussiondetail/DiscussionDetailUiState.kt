package com.team.todoktodok.presentation.xml.discussiondetail

import com.team.domain.model.Discussion

data class DiscussionDetailUiState(
    val discussion: Discussion,
    val isMyDiscussion: Boolean,
    val isLoading: Boolean = false,
)
