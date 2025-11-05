package com.team.ui_xml.discussiondetail.model

import com.team.domain.model.Comment
import kotlinx.coroutines.Job

data class CommentItemUiState(
    val comment: Comment,
    val isMyComment: Boolean,
    val itemJob: Job? = null,
)
