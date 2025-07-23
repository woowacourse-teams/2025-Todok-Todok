package com.example.todoktodok.presentation.view.discussion.detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Comment
import com.example.todoktodok.R
import com.example.todoktodok.databinding.ItemCommentBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class CommentViewHolder private constructor(
    private val binding: ItemCommentBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(comment: Comment) {
        with(binding) {
            tvUserNickname.text = comment.writer.nickname.value
            tvCommentCreateAt.text = formatDate(binding.root.context, comment.createAt)
            tvCommentContent.text = comment.content
        }
    }

    private fun formatDate(
        context: Context,
        date: LocalDateTime,
    ): String {
        val pattern = context.getString(R.string.date_format_pattern)
        val formatter = DateTimeFormatter.ofPattern(pattern, Locale.KOREA)
        return date.format(formatter)
    }

    companion object {
        fun CommentViewHolder(parent: ViewGroup): CommentViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCommentBinding.inflate(layoutInflater, parent, false)
            return CommentViewHolder(binding)
        }
    }
}
