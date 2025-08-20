package com.team.todoktodok.presentation.view.discussions.my.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemMyCreatedDiscussionBinding
import com.team.todoktodok.presentation.view.discussions.adapter.DiscussionAdapter

class CreatedDiscussionViewHolder private constructor(
    binding: ItemMyCreatedDiscussionBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    private val discussionAdapter = DiscussionAdapter(handler)

    init {
        with(binding) {
            rvCreatedDiscussionHeader.setOnClickListener {
                handler.onClickMyCreatedDiscussionHeader()
            }

            rvCreatedDiscussion.adapter = discussionAdapter
            rvCreatedDiscussion.setHasFixedSize(true)
        }
    }

    fun bind(item: MyDiscussionItems.CreatedDiscussionItem) {
        discussionAdapter.submitList(item.items)
    }

    companion object {
        fun CreatedDiscussionViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): CreatedDiscussionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemMyCreatedDiscussionBinding.inflate(inflater, parent, false)
            return CreatedDiscussionViewHolder(binding, handler)
        }
    }

    interface Handler : DiscussionAdapter.Handler {
        fun onClickMyCreatedDiscussionHeader()
    }
}
