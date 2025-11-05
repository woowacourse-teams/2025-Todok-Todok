package com.team.ui_xml.discussiondetail.comments.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.ui_xml.discussiondetail.model.CommentItemUiState

class CommentAdapter(
    private val handler: Handler,
) : ListAdapter<CommentItemUiState, CommentViewHolder>(
        commentItemUiModelsDiffUtil,
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CommentViewHolder = CommentViewHolder.Companion.CommentViewHolder(parent, handler)

    override fun onBindViewHolder(
        holder: CommentViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position])
    }

    interface Handler : CommentViewHolder.Handler
}
