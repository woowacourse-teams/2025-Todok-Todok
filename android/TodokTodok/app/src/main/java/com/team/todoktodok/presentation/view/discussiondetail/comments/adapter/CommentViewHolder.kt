package com.team.todoktodok.presentation.view.discussiondetail.comments.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.Comment
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ItemCommentBinding
import com.team.todoktodok.presentation.core.ext.formatWithResource

class CommentViewHolder private constructor(
    private val binding: ItemCommentBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(comment: Comment) {
        with(binding) {
            tvDiscussionOpinion.text = comment.content
            tvUserNickname.text = comment.writer.nickname.value
            tvDiscussionCreateAt.text =
                comment.createAt.formatWithResource(
                    binding.root.context,
                    R.string.date_format_pattern,
                )
            ivReply.setOnClickListener {
                handler.onReplyClick(comment.id)
            }
            ivCommentOption.setOnClickListener {
                handler.onOptionClick(comment.id, ivCommentOption)
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

        fun onDeleteClick(commentId: Long)

        fun onOptionClick(
            commentId: Long,
            view: View,
        )
    }
}
