package com.example.todoktodok.presentation.view.discussion.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.model.DiscussionRoom

class DiscussionRoomAdapter : ListAdapter<DiscussionRoom, DiscussionRoomViewHolder>(
    discussionRoomsDiffUtil
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionRoomViewHolder =
        DiscussionRoomViewHolder.create(parent)

    override fun onBindViewHolder(holder: DiscussionRoomViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}