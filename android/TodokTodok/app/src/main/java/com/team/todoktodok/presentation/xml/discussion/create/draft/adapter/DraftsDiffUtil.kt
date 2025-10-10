package com.team.todoktodok.presentation.xml.discussion.create.draft.adapter

import androidx.recyclerview.widget.DiffUtil
import com.team.domain.model.discussionroom.DiscussionRoom

class DraftsDiffUtil : DiffUtil.ItemCallback<DiscussionRoom>() {
    override fun areItemsTheSame(
        oldItem: DiscussionRoom,
        newItem: DiscussionRoom,
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: DiscussionRoom,
        newItem: DiscussionRoom,
    ): Boolean = oldItem.title == newItem.title
}
