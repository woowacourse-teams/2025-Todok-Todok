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
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(comment: Comment) {
        with(binding) {
            tvUserNickname.text = comment.writer.nickname.value
            tvDiscussionCreateAt.text =
                comment.createAt.formatWithResource(root.context, R.string.date_format_pattern)
            tvDiscussionOpinion.text = comment.content
        }
    }

    companion object {
        fun CommentItemViewHolder(parent: ViewGroup): CommentItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCommentDetailInformationBinding.inflate(layoutInflater, parent, false)
            return CommentItemViewHolder(binding)
        }
    }
}
