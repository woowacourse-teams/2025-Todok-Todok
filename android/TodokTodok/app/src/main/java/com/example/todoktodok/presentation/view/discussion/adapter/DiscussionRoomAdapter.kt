package com.example.todoktodok.presentation.view.discussion.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.model.DiscussionRoom
import com.example.todoktodok.presentation.view.discussion.adapter.DiscussionRoomViewHolder.Companion.DiscussionRoomViewHolder

class DiscussionRoomAdapter(
    private val navigateDiscussionRoomClick: OnDiscussionRoomListener,
) : ListAdapter<DiscussionRoom, DiscussionRoomViewHolder>(
        discussionRoomsDiffUtil,
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DiscussionRoomViewHolder = DiscussionRoomViewHolder(parent, navigateDiscussionRoomClick)

    override fun onBindViewHolder(
        holder: DiscussionRoomViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position])
    }
}
