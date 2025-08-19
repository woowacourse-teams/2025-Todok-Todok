package com.team.todoktodok.presentation.view.discussions.adapter

import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemDiscussionBinding
import com.team.todoktodok.presentation.core.ext.loadImage
import com.team.todoktodok.presentation.view.discussions.DiscussionUiState

class DiscussionViewHolder private constructor(
    private val binding: ItemDiscussionBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            handler.onItemClick(bindingAdapterPosition)
        }
    }

    fun bind(item: DiscussionUiState) {
        with(binding) {
            tvBookTitle.text = item.bookTitle
            tvBookAuthor.text = item.bookAuthor
            ivBook.loadImage(item.bookImage)

            tvDiscussionTitle.text = item.discussionTitle
            tvDiscussionWriterNickname.text = item.writerNickname

            if (item.opinionVisibility) {
                tvDiscussionOpinion.visibility = VISIBLE
                tvDiscussionOpinion.text = item.discussionOpinion
            }

            tvLikeCount.text = item.likeCount
            tvCommentCount.text = item.commentCount
            tvViews.text = item.likeCount
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
