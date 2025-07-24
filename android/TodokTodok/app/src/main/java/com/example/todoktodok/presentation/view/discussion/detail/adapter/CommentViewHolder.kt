package com.example.todoktodok.presentation.view.discussion.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Comment
import com.example.todoktodok.R
import com.example.todoktodok.databinding.ItemCommentBinding
import com.example.todoktodok.presentation.core.ext.formatWithResource

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
