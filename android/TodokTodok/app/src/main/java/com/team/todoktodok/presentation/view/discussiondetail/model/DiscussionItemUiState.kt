package com.team.todoktodok.presentation.view.discussiondetail.model

import com.team.domain.model.Discussion

data class DiscussionItemUiState(
    val discussion: Discussion,
    val isMyDiscussion: Boolean,
)
