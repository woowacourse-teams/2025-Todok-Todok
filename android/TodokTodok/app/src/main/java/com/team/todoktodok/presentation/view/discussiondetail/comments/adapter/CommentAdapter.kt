package com.team.todoktodok.presentation.view.discussiondetail.comments.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.domain.model.Comment
import com.team.todoktodok.presentation.view.discussiondetail.comments.adapter.CommentViewHolder.Companion.CommentViewHolder

class CommentAdapter(
    private val handler: Handler,
) : ListAdapter<Comment, CommentViewHolder>(
        commentsDiffUtil,
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CommentViewHolder = CommentViewHolder(parent, handler)

    override fun onBindViewHolder(
        holder: CommentViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position])
    }

    interface Handler : CommentViewHolder.Handler
}
