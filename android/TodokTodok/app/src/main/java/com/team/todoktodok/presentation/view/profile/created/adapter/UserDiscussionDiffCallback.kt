package com.team.todoktodok.presentation.view.profile.created.adapter

import androidx.recyclerview.widget.DiffUtil
import com.team.domain.model.Discussion

class UserDiscussionDiffCallback : DiffUtil.ItemCallback<Discussion>() {
    override fun areItemsTheSame(oldItem: Discussion, newItem: Discussion): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Discussion, newItem: Discussion): Boolean {
        return oldItem == newItem
    }
}
