package com.team.todoktodok.presentation.xml.discussions.hot.adapter

import androidx.recyclerview.widget.DiffUtil

class HotDiscussionDiffUtil : DiffUtil.ItemCallback<HotDiscussionItems>() {
    override fun areItemsTheSame(
        oldItem: HotDiscussionItems,
        newItem: HotDiscussionItems,
    ): Boolean = oldItem.viewType == newItem.viewType

    override fun areContentsTheSame(
        oldItem: HotDiscussionItems,
        newItem: HotDiscussionItems,
    ): Boolean = oldItem == newItem
}
