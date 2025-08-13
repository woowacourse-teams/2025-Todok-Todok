package com.team.todoktodok.presentation.view.discussiondetail.comments.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.todoktodok.presentation.view.discussiondetail.comments.adapter.CommentViewHolder.Companion.CommentViewHolder
import com.team.todoktodok.presentation.view.discussiondetail.model.CommentItemUiState

class CommentAdapter(
    private val handler: Handler,
) : ListAdapter<CommentItemUiState, CommentViewHolder>(
        commentItemUiModelsDiffUtil,
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
