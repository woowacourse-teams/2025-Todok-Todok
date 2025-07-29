package com.team.todoktodok.presentation.utview.discussions.all.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team.domain.model.Discussion
import com.team.todoktodok.databinding.ItemUtDiscussionBinding

class DiscussionViewHolder private constructor(
    private val binding: ItemUtDiscussionBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            handler.onItemClick(bindingAdapterPosition)
        }
    }

    fun bind(item: Discussion) {
        with(binding) {
            tvBookTitle.text = item.book.title
            tvDiscussionTitle.text = item.discussionTitle
            tvDiscussionContent.text = item.discussionOpinion

            Glide
                .with(binding.root.context)
                .load(item.book.image)
                .into(ivBook)
        }
    }

    companion object {
        fun DiscussionViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): DiscussionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemUtDiscussionBinding.inflate(inflater, parent, false)
            return DiscussionViewHolder(binding, handler)
        }
    }

    interface Handler {
        fun onItemClick(index: Int)
    }
}
