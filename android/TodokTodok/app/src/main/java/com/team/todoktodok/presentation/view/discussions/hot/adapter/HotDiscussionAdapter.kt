package com.team.todoktodok.presentation.view.discussions.hot.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.presentation.view.discussions.hot.adapter.HotActivatedDiscussionViewHolder.Companion.HotActivatedDiscussionViewHolder
import com.team.todoktodok.presentation.view.discussions.hot.adapter.HotDiscussionItems.ViewType.Companion.ViewType
import com.team.todoktodok.presentation.view.discussions.hot.adapter.HotPopularDiscussionViewHolder.Companion.HotPopularDiscussionViewHolder

class HotDiscussionAdapter(
    private val handler: Handler,
) : ListAdapter<HotDiscussionItems, RecyclerView.ViewHolder>(HotDiscussionDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (ViewType(index = viewType)) {
            HotDiscussionItems.ViewType.POPULAR -> HotPopularDiscussionViewHolder(parent, handler)
            HotDiscussionItems.ViewType.ACTIVATED -> HotActivatedDiscussionViewHolder(parent, handler)
        }

    override fun getItemViewType(position: Int): Int = getItem(position).viewType.sequence

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is HotDiscussionItems.PopularItem -> {
                Log.d("dasdas", "PopularItem: ${item.items.size}: ")
                (holder as HotPopularDiscussionViewHolder).bind(item)
            }
            is HotDiscussionItems.ActivatedItem -> {
                Log.d("dasdas", "ActivatedItem: ${item.items.size}: ")
                (holder as HotActivatedDiscussionViewHolder).bind(item)
            }
        }
    }

    interface Handler :
        HotPopularDiscussionViewHolder.Handler,
        HotActivatedDiscussionViewHolder.Handler
}
