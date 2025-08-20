package com.team.todoktodok.presentation.view.discussions.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.todoktodok.presentation.view.discussions.DiscussionUiState

class DiscussionAdapter(
    private val handler: Handler,
) : ListAdapter<DiscussionUiState, DiscussionViewHolder>(DiscussionDiffUtil()) {
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
