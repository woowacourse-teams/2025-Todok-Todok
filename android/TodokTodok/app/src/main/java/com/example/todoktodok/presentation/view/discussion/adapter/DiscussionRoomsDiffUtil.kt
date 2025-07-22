package com.example.todoktodok.presentation.view.discussion.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.DiscussionRoom

val discussionRoomsDiffUtil =
    object : DiffUtil.ItemCallback<DiscussionRoom>() {
        override fun areItemsTheSame(
            oldItem: DiscussionRoom,
            newItem: DiscussionRoom,
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: DiscussionRoom,
            newItem: DiscussionRoom,
        ): Boolean = oldItem == newItem
    }
