package com.team.todoktodok.presentation.view.discussiondetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.Comment
import com.team.todoktodok.databinding.ItemCommentBinding

class CommentViewHolder private constructor(
    private val binding: ItemCommentBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(comment: Comment) {
    }

    companion object {
        fun CommentViewHolder(parent: ViewGroup): CommentViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCommentBinding.inflate(layoutInflater, parent, false)
            return CommentViewHolder(binding)
        }
    }
}
