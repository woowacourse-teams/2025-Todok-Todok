package com.team.todoktodok.presentation.view.discussions.hot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemHotPopularDiscussionBinding
import com.team.todoktodok.presentation.view.discussions.adapter.DiscussionAdapter

class HotPopularDiscussionViewHolder(
    private val binding: ItemHotPopularDiscussionBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: HotDiscussionItems.PopularItem) {
        val discussionAdapter = DiscussionAdapter(handler)
        val manager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        with(binding.rvPopularDiscussion) {
            adapter = discussionAdapter
            layoutManager = manager
        }
        discussionAdapter.submitList(item.items)
    }

    companion object {
        fun HotPopularDiscussionViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): HotPopularDiscussionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemHotPopularDiscussionBinding.inflate(inflater, parent, false)
            return HotPopularDiscussionViewHolder(binding, handler)
        }
    }

    interface Handler : DiscussionAdapter.Handler
}
