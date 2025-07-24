package com.example.todoktodok.presentation.view.discussion.discussions.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.model.Discussion
import com.example.todoktodok.presentation.view.discussion.discussions.adapter.DiscussionViewHolder.Companion.DiscussionViewHolder

class DiscussionAdapter(
    private val navigateDiscussionClick: OnDiscussionListener,
) : ListAdapter<Discussion, DiscussionViewHolder>(
        discussionsDiffUtil,
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DiscussionViewHolder = DiscussionViewHolder(parent, navigateDiscussionClick)

    override fun onBindViewHolder(
        holder: DiscussionViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position])
    }
}
