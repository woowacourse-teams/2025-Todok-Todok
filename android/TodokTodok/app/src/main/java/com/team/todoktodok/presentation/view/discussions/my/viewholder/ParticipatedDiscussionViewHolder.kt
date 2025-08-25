package com.team.todoktodok.presentation.view.discussions.my.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemMyParticipatedDiscussionBinding
import com.team.todoktodok.presentation.view.discussions.adapter.DiscussionAdapter
import com.team.todoktodok.presentation.view.discussions.my.adapter.MyDiscussionItems

class ParticipatedDiscussionViewHolder private constructor(
    binding: ItemMyParticipatedDiscussionBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    private val discussionAdapterHandler =
        object : DiscussionAdapter.Handler {
            override fun onItemClick(index: Int) {
                val discussionId = discussionAdapter.currentList[index].discussionId
                handler.onClickMyParticipatedDiscussionItem(discussionId)
            }
        }
    private val discussionAdapter: DiscussionAdapter = DiscussionAdapter(discussionAdapterHandler)

    init {
        with(binding) {
            rvParticipatedDiscussionHeader.setOnClickListener {
                handler.onClickMyParticipatedDiscussionHeader()
            }

            rvParticipatedDiscussion.adapter = discussionAdapter
            rvParticipatedDiscussion.setHasFixedSize(true)
        }
    }

    fun bind(item: MyDiscussionItems.ParticipatedDiscussionItem) {
        discussionAdapter.submitList(item.items)
    }

    companion object {
        fun ParticipatedDiscussionViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): ParticipatedDiscussionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemMyParticipatedDiscussionBinding.inflate(inflater, parent, false)
            return ParticipatedDiscussionViewHolder(binding, handler)
        }
    }

    interface Handler {
        fun onClickMyParticipatedDiscussionHeader()

        fun onClickMyParticipatedDiscussionItem(discussionId: Long)
    }
}
