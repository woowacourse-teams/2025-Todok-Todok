package com.team.todoktodok.presentation.view.discussiondetail.comments.adapter

import androidx.recyclerview.widget.DiffUtil
import com.team.todoktodok.presentation.view.discussiondetail.comments.model.CommentUiModel

val commentUiModelsDiffUtil =
    object : DiffUtil.ItemCallback<CommentUiModel>() {
        override fun areItemsTheSame(
            oldItem: CommentUiModel,
            newItem: CommentUiModel,
        ): Boolean = oldItem.comment.id == newItem.comment.id

        override fun areContentsTheSame(
            oldItem: CommentUiModel,
            newItem: CommentUiModel,
        ): Boolean = oldItem == newItem
    }
