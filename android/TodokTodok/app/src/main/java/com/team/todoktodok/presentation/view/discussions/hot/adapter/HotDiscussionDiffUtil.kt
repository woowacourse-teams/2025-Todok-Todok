package com.team.todoktodok.presentation.view.discussions.hot.adapter

import androidx.recyclerview.widget.DiffUtil

class HotDiscussionDiffUtil : DiffUtil.ItemCallback<HotDiscussionItems>() {
    override fun areItemsTheSame(
        oldItem: HotDiscussionItems,
        newItem: HotDiscussionItems,
    ): Boolean = oldItem.javaClass == newItem.javaClass

    override fun areContentsTheSame(
        oldItem: HotDiscussionItems,
        newItem: HotDiscussionItems,
    ): Boolean = oldItem == newItem
}
