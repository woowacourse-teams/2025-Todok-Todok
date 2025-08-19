package com.team.todoktodok.presentation.view.discussions.all.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.Discussion
import com.team.todoktodok.databinding.ItemDiscussionBinding
import com.team.todoktodok.presentation.core.ext.loadImage

class DiscussionViewHolder private constructor(
    private val binding: ItemDiscussionBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            handler.onItemClick(bindingAdapterPosition)
        }
    }

    fun bind(item: Discussion) {
        with(binding) {
            tvBookTitle.text = item.getBookTitle()
            tvBookAuthor.text = item.getBookAuthor()
            ivBook.loadImage(item.bookImage)

            tvDiscussionTitle.text = item.discussionTitle
            tvDiscussionWriterNickname.text = item.writerNickname
        }
    }

    companion object {
        fun DiscussionViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): DiscussionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemDiscussionBinding.inflate(inflater, parent, false)
            return DiscussionViewHolder(binding, handler)
        }
    }

    interface Handler {
        fun onItemClick(index: Int)
    }
}
