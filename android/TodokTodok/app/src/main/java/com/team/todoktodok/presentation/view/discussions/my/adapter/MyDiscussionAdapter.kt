package com.team.todoktodok.presentation.view.discussions.my.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.presentation.view.discussions.my.adapter.CreatedDiscussionViewHolder.Companion.CreatedDiscussionViewHolder
import com.team.todoktodok.presentation.view.discussions.my.adapter.MyDiscussionItems.ViewType.Companion.ViewType
import com.team.todoktodok.presentation.view.discussions.my.adapter.ParticipatedDiscussionViewHolder.Companion.ParticipatedDiscussionViewHolder

class MyDiscussionAdapter(
    private val handler: Handler,
) : ListAdapter<MyDiscussionItems, RecyclerView.ViewHolder>(MyDiscussionDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (ViewType(index = viewType)) {
            MyDiscussionItems.ViewType.CREATED_DISCUSSIONS -> CreatedDiscussionViewHolder(parent, handler)
            MyDiscussionItems.ViewType.PARTICIPATED_DISCUSSIONS -> ParticipatedDiscussionViewHolder(parent, handler)
        }

    override fun getItemViewType(position: Int): Int = getItem(position).viewType.sequence

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        TODO("Not yet implemented")
    }

    interface Handler :
        CreatedDiscussionViewHolder.Handler,
        ParticipatedDiscussionViewHolder.Handler
}
