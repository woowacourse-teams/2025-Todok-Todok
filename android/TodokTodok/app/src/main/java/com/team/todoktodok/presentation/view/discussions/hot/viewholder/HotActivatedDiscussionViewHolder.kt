package com.team.todoktodok.presentation.view.discussions.hot.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemHotActivatedDiscussionBinding
import com.team.todoktodok.presentation.view.discussions.adapter.DiscussionAdapter
import com.team.todoktodok.presentation.view.discussions.hot.adapter.HotDiscussionItems

class HotActivatedDiscussionViewHolder private constructor(
    binding: ItemHotActivatedDiscussionBinding,
    handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    private val discussionAdapter = DiscussionAdapter(handler)

    init {
        with(binding) {
            rvActivatedDiscussion.adapter = discussionAdapter
            rvActivatedDiscussion.setHasFixedSize(true)
        }
    }

    fun bind(item: HotDiscussionItems.ActivatedItem) {
        discussionAdapter.submitList(item.items)
    }

    companion object {
        fun HotActivatedDiscussionViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): HotActivatedDiscussionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemHotActivatedDiscussionBinding.inflate(inflater, parent, false)
            return HotActivatedDiscussionViewHolder(binding, handler)
        }
    }

    interface Handler : DiscussionAdapter.Handler
}
