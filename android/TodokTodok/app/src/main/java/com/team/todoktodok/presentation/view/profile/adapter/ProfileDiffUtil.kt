package com.team.todoktodok.presentation.view.profile.adapter

import androidx.recyclerview.widget.DiffUtil

class ProfileDiffUtil : DiffUtil.ItemCallback<ProfileItems>() {
    override fun areItemsTheSame(
        oldItem: ProfileItems,
        newItem: ProfileItems,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: ProfileItems,
        newItem: ProfileItems,
    ): Boolean = oldItem == newItem
}
