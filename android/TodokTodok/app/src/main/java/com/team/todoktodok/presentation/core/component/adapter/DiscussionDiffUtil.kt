package com.team.todoktodok.presentation.core.component.adapter

import androidx.recyclerview.widget.DiffUtil
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiModel

class DiscussionDiffUtil : DiffUtil.ItemCallback<DiscussionUiModel>() {
    override fun areItemsTheSame(
        oldItem: DiscussionUiModel,
        newItem: DiscussionUiModel,
    ): Boolean = oldItem.discussionId == newItem.discussionId

    override fun areContentsTheSame(
        oldItem: DiscussionUiModel,
        newItem: DiscussionUiModel,
    ): Boolean = oldItem == newItem
}
