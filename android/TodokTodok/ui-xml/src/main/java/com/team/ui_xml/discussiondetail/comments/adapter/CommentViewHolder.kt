package com.team.ui_xml.discussiondetail.comments.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.ui_xml.databinding.ItemCommentBinding
import com.team.ui_xml.discussiondetail.model.CommentItemUiState
import com.team.ui_xml.extension.loadCircleImage
import com.team.ui_xml.extension.toRelativeString

class CommentViewHolder private constructor(
    private val binding: ItemCommentBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(commentItemUiState: CommentItemUiState) {
        with(binding) {
            tvCommentOpinion.text = commentItemUiState.comment.content
            tvUserNickname.text = commentItemUiState.comment.writer.nickname
            tvDiscussionCreateAt.text =
                commentItemUiState.comment.createAt.toRelativeString(
                    binding.root.context,
                )
            ivReply.setOnClickListener {
                handler.onReplyClick(commentItemUiState.comment.id)
            }
            ivCommentOption.setOnClickListener {
                handler.onOptionClick(commentItemUiState, ivCommentOption)
            }
            ivUserProfile.loadCircleImage(commentItemUiState.comment.writer.profileImage)
            ivUserProfile.setOnClickListener {
                handler.onClickUser(
                    commentItemUiState.comment.writer.id,
                    commentItemUiState.comment.writer.nickname,
                )
            }
            tvUserNickname.setOnClickListener {
                handler.onClickUser(
                    commentItemUiState.comment.writer.id,
                    commentItemUiState.comment.writer.nickname,
                )
            }
            ivLike.setOnClickListener { handler.onToggleLike(commentItemUiState.comment.id) }
            ivLike.isSelected = commentItemUiState.comment.isLikedByMe
            tvLikeCount.text = commentItemUiState.comment.likeCount.toString()
            tvLikeCount.setOnClickListener { handler.onToggleLike(commentItemUiState.comment.id) }
            tvReplyCount.text = commentItemUiState.comment.replyCount.toString()
            tvReplyCount.setOnClickListener {
                handler.onReplyClick(commentItemUiState.comment.id)
            }
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

        fun onClickUser(
            userId: Long,
            userName: String,
        )
    }
}
