package com.team.todoktodok.presentation.view.discussiondetail.comments.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ItemCommentBinding
import com.team.todoktodok.presentation.core.ext.formatWithResource
import com.team.todoktodok.presentation.core.ext.loadImage
import com.team.todoktodok.presentation.view.discussiondetail.model.CommentItemUiState

class CommentViewHolder private constructor(
    private val binding: ItemCommentBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(commentItemUiState: CommentItemUiState) {
        with(binding) {
            tvCommentOpinion.text = commentItemUiState.comment.content
            tvUserNickname.text = commentItemUiState.comment.writer.nickname.value
            tvDiscussionCreateAt.text =
                commentItemUiState.comment.createAt.formatWithResource(
                    binding.root.context,
                    R.string.date_format_pattern,
                )
            ivReply.setOnClickListener {
                handler.onReplyClick(commentItemUiState.comment.id)
            }
            ivCommentOption.setOnClickListener {
                handler.onOptionClick(commentItemUiState, ivCommentOption)
            }
            ivUserProfile.loadImage("")
            ivUserProfile.setOnClickListener {
                handler.onClickUser(commentItemUiState.comment.writer.id)
            }
            tvUserNickname.setOnClickListener {
                handler.onClickUser(commentItemUiState.comment.writer.id)
            }
            ivLike.setOnClickListener { handler.onToggleLike(commentItemUiState.comment.id) }
            ivLike.isSelected = commentItemUiState.comment.isLikedByMe
            tvHeartCount.text = commentItemUiState.comment.likeCount.toString()
            tvReplyCount.text = commentItemUiState.comment.replyCount.toString()
        }
    }

    companion object {
        fun CommentViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): CommentViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCommentBinding.inflate(layoutInflater, parent, false)
            return CommentViewHolder(binding, handler)
        }
    }

    interface Handler {
        fun onReplyClick(commentId: Long)

        fun onOptionClick(
            commentItemUiState: CommentItemUiState,
            view: View,
        )

        fun onToggleLike(commentId: Long)

        fun onClickUser(userId: Long)
    }
}
