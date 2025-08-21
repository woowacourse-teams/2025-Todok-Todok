package com.team.todoktodok.presentation.view.discussions.hot.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemHotPopularDiscussionBinding
import com.team.todoktodok.presentation.view.discussions.adapter.DiscussionAdapter
import com.team.todoktodok.presentation.view.discussions.hot.adapter.HotDiscussionItems

class HotPopularDiscussionViewHolder private constructor(
    binding: ItemHotPopularDiscussionBinding,
    handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    private val discussionAdapter = DiscussionAdapter(handler)
    private val manager =
        LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

    init {
        with(binding) {
            rvPopularDiscussion.adapter = discussionAdapter
            rvPopularDiscussion.layoutManager = manager
        }
    }

    fun bind(item: HotDiscussionItems.PopularItem) {
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
