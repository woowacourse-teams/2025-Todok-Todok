package com.team.todoktodok.presentation.view.discussion.detail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.domain.model.Comment
import com.team.todoktodok.presentation.view.discussion.detail.adapter.CommentViewHolder.Companion.CommentViewHolder

class CommentAdapter :
    ListAdapter<Comment, CommentViewHolder>(
        commentsDiffUtil,
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CommentViewHolder = CommentViewHolder(parent)

    override fun onBindViewHolder(
        holder: CommentViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position])
    }
}
