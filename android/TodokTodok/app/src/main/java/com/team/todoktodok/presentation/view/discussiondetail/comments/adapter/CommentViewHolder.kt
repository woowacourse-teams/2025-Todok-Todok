package com.team.todoktodok.presentation.view.discussiondetail.comments.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ItemCommentBinding
import com.team.todoktodok.presentation.core.ext.formatWithResource
import com.team.todoktodok.presentation.view.discussiondetail.model.CommentUiModel

class CommentViewHolder private constructor(
    private val binding: ItemCommentBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(commentUiModel: CommentUiModel) {
        with(binding) {
            tvCommentOpinion.text = commentUiModel.comment.content
            tvUserNickname.text = commentUiModel.comment.writer.nickname.value
            tvDiscussionCreateAt.text =
                commentUiModel.comment.createAt.formatWithResource(
                    binding.root.context,
                    R.string.date_format_pattern,
                )
            ivReply.setOnClickListener {
                handler.onReplyClick(commentUiModel.comment.id)
            }
            ivCommentOption.setOnClickListener {
                handler.onOptionClick(commentUiModel, ivCommentOption)
            }
            tvUserNickname.setOnClickListener {
                handler.onClickUserName(commentUiModel.comment.writer.id)
            }
            ivLike.setOnClickListener { handler.onToggleLike(commentUiModel.comment.id) }
            ivLike.isSelected = commentUiModel.comment.isLikedByMe
            tvHeartCount.text = commentUiModel.comment.likeCount.toString()
            tvReplyCount.text = commentUiModel.comment.replyCount.toString()
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
            commentUiModel: CommentUiModel,
            view: View,
        )

        fun onToggleLike(commentId: Long)

        fun onClickUserName(userId: Long)
    }
}
