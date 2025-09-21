package com.team.todoktodok.presentation.xml.discussions.my.adapter

import androidx.recyclerview.widget.DiffUtil

class MyDiscussionDiffUtil : DiffUtil.ItemCallback<MyDiscussionItems>() {
    override fun areItemsTheSame(
        oldItem: MyDiscussionItems,
        newItem: MyDiscussionItems,
    ): Boolean = oldItem.viewType == newItem.viewType

    override fun areContentsTheSame(
        oldItem: MyDiscussionItems,
        newItem: MyDiscussionItems,
    ): Boolean = oldItem == newItem
}
