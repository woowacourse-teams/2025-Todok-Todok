package com.team.todoktodok.presentation.view.profile.created.adapter

import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemDiscussionBinding
import com.team.todoktodok.presentation.core.ext.loadImage
import com.team.todoktodok.presentation.view.discussions.DiscussionUiState

class UserDiscussionViewHolder private constructor(
    private val binding: ItemDiscussionBinding,
    val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            handler.onSelectDiscussion(absoluteAdapterPosition)
        }
    }

    fun bind(item: DiscussionUiState) {
        with(binding) {
            tvBookTitle.text = item.bookTitle
            tvBookAuthor.text = item.bookAuthor
            ivBook.loadImage(item.bookImage)

            tvDiscussionTitle.text = item.discussionTitle

            if (item.writerNicknameVisibility) {
                tvDiscussionWriterNickname.text = item.writerNickname
            }

            if (item.opinionVisibility) {
                tvDiscussionOpinion.visibility = VISIBLE
                tvDiscussionOpinion.text = item.discussionOpinion
            } else {
                tvDiscussionOpinion.maxLines = 1
                tvDiscussionOpinion.minLines = 1
            }

            tvLikeCount.text = item.likeCount
            tvCommentCount.text = item.commentCount
            tvViews.text = item.viewCount
        }
    }

    companion object {
        fun UserDiscussionViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): UserDiscussionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemDiscussionBinding.inflate(inflater, parent, false)
            return UserDiscussionViewHolder(binding, handler)
        }
    }

    interface Handler {
        fun onSelectDiscussion(index: Int)
    }
}
