package com.team.todoktodok.presentation.view.discussiondetail.model

import com.team.domain.model.Discussion

data class DiscussionUiModel(
    val discussion: Discussion,
    val isMyDiscussion: Boolean,
)
