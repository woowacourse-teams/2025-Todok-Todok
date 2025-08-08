package com.team.todoktodok.presentation.view.discussiondetail.commentdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.Comment
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ItemCommentDetailInformationBinding
import com.team.todoktodok.presentation.core.ext.formatWithResource

class CommentItemViewHolder private constructor(
    private val binding: ItemCommentDetailInformationBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(comment: Comment) {
        with(binding) {
            tvUserNickname.text = comment.writer.nickname.value
            tvDiscussionCreateAt.text =
                comment.createAt.formatWithResource(root.context, R.string.date_format_pattern)
            tvDiscussionOpinion.text = comment.content
            ivLike.isSelected = comment.isLikedByMe
            ivLike.setOnClickListener { handler.onCommentLikeClick(comment.id) }
            tvHeartCount.text = comment.likeCount.toString()
            tvUserNickname.setOnClickListener {
                handler.onCommentUserNameClick(comment.writer.id)
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
        fun onCommentLikeClick(commentId: Long)

        fun onCommentUserNameClick(userId: Long)
    }
}
