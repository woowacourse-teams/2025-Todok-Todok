package com.team.todoktodok.presentation.xml.discussiondetail.commentdetail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemCommentDetailInformationBinding
import com.team.todoktodok.presentation.core.ext.loadCircleImage
import com.team.todoktodok.presentation.core.ext.toRelativeString

class CommentItemViewHolder private constructor(
    private val binding: ItemCommentDetailInformationBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(commentItem: CommentDetailItems.CommentItem) {
        val comment = commentItem.value.comment
        with(binding) {
            tvUserNickname.text = comment.writer.nickname.value
            tvDiscussionCreateAt.text =
                comment.createAt.toRelativeString(
                    root.context,
                )
            tvCommentOpinion.text = comment.content
            ivLike.isSelected = comment.isLikedByMe
            ivLike.setOnClickListener { handler.onClickCommentLike() }
            tvLikeCount.text = comment.likeCount.toString()
            tvLikeCount.setOnClickListener { handler.onClickCommentLike() }
            ivUserProfile.loadCircleImage(comment.writer.profileImage)
            ivUserProfile.setOnClickListener {
                handler.onClickCommentUserName(comment.writer.id)
            }
            tvUserNickname.setOnClickListener {
                handler.onClickCommentUserName(comment.writer.id)
            }
            ivCommentOption.setOnClickListener {
                handler.onClickCommentOption(commentItem, ivCommentOption)
            }
        }
    }

    companion object {
        fun CommentItemViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): CommentItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCommentDetailInformationBinding.inflate(layoutInflater, parent, false)
            return CommentItemViewHolder(binding, handler)
        }
    }

    interface Handler {
        fun onClickCommentLike()

        fun onClickCommentUserName(userId: Long)

        fun onClickCommentOption(
            item: CommentDetailItems.CommentItem,
            anchorView: View,
        )
    }
}
