package com.team.todoktodok.presentation.xml.discussiondetail.model

import com.team.domain.model.Discussion

data class DiscussionDetailItemUiState(
    val discussion: Discussion,
    val requestVersion: Int,
    val isMyDiscussion: Boolean,
)
