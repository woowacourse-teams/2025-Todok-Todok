package com.team.todoktodok.presentation.view.discussions.all.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.domain.model.Discussion

class DiscussionAdapter(
    private val handler: Handler,
) : ListAdapter<Discussion, DiscussionViewHolder>(DiscussionDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DiscussionViewHolder = DiscussionViewHolder.DiscussionViewHolder(parent, handler)

    override fun onBindViewHolder(
        holder: DiscussionViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    interface Handler : DiscussionViewHolder.Handler
}
