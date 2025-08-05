package com.team.todoktodok.presentation.view.profile.created.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.view.profile.created.adapter.UserDiscussionViewHolder.Companion.UserDiscussionViewHolder

class UserDiscussionAdapter(
    private val handler: Handler,
) : ListAdapter<Discussion, UserDiscussionViewHolder>(UserDiscussionDiffCallback()) {


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
