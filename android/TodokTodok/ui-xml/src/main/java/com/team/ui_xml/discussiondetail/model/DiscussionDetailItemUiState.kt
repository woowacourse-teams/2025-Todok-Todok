package com.team.ui_xml.discussiondetail.model

import com.team.domain.model.Discussion

data class DiscussionDetailItemUiState(
    val discussion: Discussion,
    val requestVersion: Int,
    val isMyDiscussion: Boolean,
)
