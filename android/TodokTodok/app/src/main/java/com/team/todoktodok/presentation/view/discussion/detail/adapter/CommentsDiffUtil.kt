package com.team.todoktodok.presentation.view.discussion.detail.adapter

import androidx.recyclerview.widget.DiffUtil
import com.team.domain.model.Comment

val commentsDiffUtil =
    object : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(
            oldItem: Comment,
            newItem: Comment,
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Comment,
            newItem: Comment,
        ): Boolean = oldItem == newItem
    }
