package com.team.ui_xml.component.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.Discussion
import com.team.ui_xml.databinding.ItemDiscussionBinding
import com.team.ui_xml.extension.loadCircleImage
import com.team.ui_xml.extension.loadImage
import com.team.core.R as coreR

abstract class BaseDiscussionViewHolder(
    protected val binding: ItemDiscussionBinding,
    protected val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            handler.onItemClick(bindingAdapterPosition)
        }
    }

    fun bind(item: Discussion) {
        bindBase(item)
        bindExtra(item)
    }

    private fun bindBase(item: Discussion) =
        with(binding) {
            tvDiscussionTitle.text = item.discussionTitle
            tvBookTitle.text = item.getBookTitle()
            tvBookAuthor.text = item.getBookAuthor()
            ivBook.loadImage(item.bookImage)
            tvLikeCount.text = item.likeCount.toString()
            tvCommentCount.text = item.commentCount.toString()
            tvViews.text = item.viewCount.toString()
            ivWriterProfileImage.loadCircleImage(item.writer.profileImage)
            tvDiscussionWriterNickname.text = item.writerNickname

            drawLikeButton(item.isLikedByMe)
        }

    protected abstract fun bindExtra(item: Discussion)

    private fun drawLikeButton(isLikedByMe: Boolean) {
        val iconRes = if (isLikedByMe) coreR.drawable.btn_heart_filled else coreR.drawable.btn_heart_empty
        binding.ivLikeCount.setImageDrawable(
            ContextCompat.getDrawable(itemView.context, iconRes),
        )
    }

    enum class ViewHolderType {
        DEFAULT,
        WRITER_HIDDEN,
        RESIZING,
    }

    fun interface Handler {
        fun onItemClick(index: Int)
    }
}
