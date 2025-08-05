package com.team.todoktodok.presentation.view.profile.created.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.view.profile.created.adapter.UserDiscussionViewHolder.Companion.UserDiscussionViewHolder

class UserDiscussionAdapter(
    private val handler: Handler,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<Discussion>()

    fun submitList(newItems: List<Discussion>) {
        items.clear()
        items.addAll(newItems)
        notifyItemRangeInserted(0, newItems.size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder = UserDiscussionViewHolder(parent, handler)

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        (holder as UserDiscussionViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    interface Handler : UserDiscussionViewHolder.Handler
}
