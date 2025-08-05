package com.team.todoktodok.presentation.view.profile.created.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.view.profile.created.adapter.UserDiscussionViewHolder.Companion.UserDiscussionViewHolder

class UserDiscussionAdapter(
    private val handler: Handler,
) : ListAdapter<Discussion, RecyclerView.ViewHolder>(UserDiscussionDiffCallback()) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder = UserDiscussionViewHolder(parent, handler)

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        (holder as UserDiscussionViewHolder).bind(getItem(position))
    }

    interface Handler : UserDiscussionViewHolder.Handler
}
