package com.team.todoktodok.presentation.xml.discussions.my.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemMyCreatedDiscussionBinding
import com.team.todoktodok.presentation.core.component.adapter.BaseDiscussionViewHolder
import com.team.todoktodok.presentation.core.component.adapter.DiscussionAdapter
import com.team.todoktodok.presentation.xml.discussions.my.adapter.MyDiscussionItems

class CreatedDiscussionViewHolder private constructor(
    binding: ItemMyCreatedDiscussionBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    private val discussionAdapterHandler: DiscussionAdapter.Handler =
        object : DiscussionAdapter.Handler {
            override fun onItemClick(index: Int) {
                val discussionId = discussionAdapter.currentList[index].discussionId
                handler.onClickMyCreatedDiscussionItem(discussionId)
            }
        }

    private val discussionAdapter: DiscussionAdapter =
        DiscussionAdapter(discussionAdapterHandler, BaseDiscussionViewHolder.ViewHolderType.DEFAULT)

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

    interface Handler {
        fun onClickMyCreatedDiscussionHeader()

        fun onClickMyCreatedDiscussionItem(discussionId: Long)
    }
}
