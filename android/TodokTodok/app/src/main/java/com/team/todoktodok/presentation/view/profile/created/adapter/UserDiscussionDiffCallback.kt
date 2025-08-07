package com.team.todoktodok.presentation.view.profile.created.adapter

import androidx.recyclerview.widget.DiffUtil
import com.team.domain.model.member.MemberDiscussion

class UserDiscussionDiffCallback : DiffUtil.ItemCallback<MemberDiscussion>() {
    override fun areItemsTheSame(
        oldItem: MemberDiscussion,
        newItem: MemberDiscussion,
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: MemberDiscussion,
        newItem: MemberDiscussion,
    ): Boolean = oldItem == newItem
}
