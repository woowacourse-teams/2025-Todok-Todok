package com.team.todoktodok.presentation.xml.discussiondetail.comments.adapter

import androidx.recyclerview.widget.DiffUtil
import com.team.todoktodok.presentation.xml.discussiondetail.model.CommentItemUiState

val commentItemUiModelsDiffUtil =
    object : DiffUtil.ItemCallback<CommentItemUiState>() {
        override fun areItemsTheSame(
            oldItem: CommentItemUiState,
            newItem: CommentItemUiState,
        ): Boolean = oldItem.comment.id == newItem.comment.id

        override fun areContentsTheSame(
            oldItem: CommentItemUiState,
            newItem: CommentItemUiState,
        ): Boolean = oldItem == newItem
    }
