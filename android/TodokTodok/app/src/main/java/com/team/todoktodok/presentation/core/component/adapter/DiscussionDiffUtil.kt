package com.team.todoktodok.presentation.core.component.adapter

import androidx.recyclerview.widget.DiffUtil
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState

class DiscussionDiffUtil : DiffUtil.ItemCallback<DiscussionUiState>() {
    override fun areItemsTheSame(
        oldItem: DiscussionUiState,
        newItem: DiscussionUiState,
    ): Boolean = oldItem.discussionId == newItem.discussionId

    override fun areContentsTheSame(
        oldItem: DiscussionUiState,
        newItem: DiscussionUiState,
    ): Boolean = oldItem == newItem
}
