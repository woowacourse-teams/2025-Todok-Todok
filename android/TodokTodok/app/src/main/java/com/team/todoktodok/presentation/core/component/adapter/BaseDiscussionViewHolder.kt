package com.team.todoktodok.presentation.core.component.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ItemDiscussionBinding
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiModel
import com.team.todoktodok.presentation.core.ext.loadCircleImage
import com.team.todoktodok.presentation.core.ext.loadImage

abstract class BaseDiscussionViewHolder(
    protected val binding: ItemDiscussionBinding,
    protected val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            handler.onItemClick(bindingAdapterPosition)
        }
    }

    fun bind(item: DiscussionUiModel) {
        bindBase(item)
        bindExtra(item)
    }

    private fun bindBase(item: DiscussionUiModel) =
        with(binding) {
            tvDiscussionTitle.text = item.discussionTitle
            tvBookTitle.text = item.bookTitle
            tvBookAuthor.text = item.bookAuthor
            ivBook.loadImage(item.bookImage)
            tvLikeCount.text = item.likeCount
            tvCommentCount.text = item.commentCount
            tvViews.text = item.viewCount
            ivWriterProfileImage.loadCircleImage(item.writerProfileImage)
            tvDiscussionWriterNickname.text = item.writerNickname

            drawLikeButton(item.isLikedByMe)
        }

    protected abstract fun bindExtra(item: DiscussionUiModel)

    private fun drawLikeButton(isLikedByMe: Boolean) {
        val iconRes = if (isLikedByMe) R.drawable.btn_heart_filled else R.drawable.btn_heart_empty
        binding.ivLikeCount.setImageDrawable(
            ContextCompat.getDrawable(itemView.context, iconRes),
        )
    }

    enum class ViewHolderType {
        DEFAULT,
        WRITER_HIDDEN,
        RESIZING,
        QUERY_HIGHLIGHTING,
    }

    fun interface Handler {
        fun onItemClick(index: Int)
    }
}
