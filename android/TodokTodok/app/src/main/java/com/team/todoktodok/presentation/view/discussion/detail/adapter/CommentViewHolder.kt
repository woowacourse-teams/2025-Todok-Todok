package com.team.todoktodok.presentation.view.discussion.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.Comment
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ItemCommentBinding
import com.team.todoktodok.presentation.core.ext.formatWithResource

class CommentViewHolder private constructor(
    private val binding: ItemCommentBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(comment: Comment) {
        with(binding) {
            tvUserNickname.text = comment.writer.nickname.value
            tvCommentCreateAt.text =
                comment.createAt.formatWithResource(
                    binding.root.context,
                    R.string.date_format_pattern,
                )
            tvCommentContent.text = comment.content
        }
    }

    companion object {
        fun CommentViewHolder(parent: ViewGroup): CommentViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCommentBinding.inflate(layoutInflater, parent, false)
            return CommentViewHolder(binding)
        }
    }
}
