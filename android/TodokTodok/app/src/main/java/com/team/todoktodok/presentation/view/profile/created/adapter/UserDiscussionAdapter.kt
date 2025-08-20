package com.team.todoktodok.presentation.view.profile.created.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.todoktodok.presentation.view.discussions.DiscussionUiState
import com.team.todoktodok.presentation.view.profile.created.adapter.UserDiscussionViewHolder.Companion.UserDiscussionViewHolder

class UserDiscussionAdapter(
    private val handler: Handler,
) : ListAdapter<DiscussionUiState, UserDiscussionViewHolder>(UserDiscussionDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): UserDiscussionViewHolder = UserDiscussionViewHolder(parent, handler)

    override fun onBindViewHolder(
        holder: UserDiscussionViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    interface Handler : UserDiscussionViewHolder.Handler
}
