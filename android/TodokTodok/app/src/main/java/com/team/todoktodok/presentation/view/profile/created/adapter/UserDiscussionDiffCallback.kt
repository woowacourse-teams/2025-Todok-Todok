package com.team.todoktodok.presentation.view.profile.created.adapter

import androidx.recyclerview.widget.DiffUtil
import com.team.domain.model.member.MemberDiscussion
import com.team.todoktodok.presentation.view.discussions.DiscussionUiState

class UserDiscussionDiffCallback : DiffUtil.ItemCallback<DiscussionUiState>() {
    override fun areItemsTheSame(
        oldItem: DiscussionUiState,
        newItem: DiscussionUiState,
    ): Boolean = oldItem.item.id == newItem.item.id

    override fun areContentsTheSame(
        oldItem: DiscussionUiState,
        newItem: DiscussionUiState,
    ): Boolean = oldItem == newItem
}
