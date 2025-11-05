package com.team.ui_xml.discussiondetail.commentdetail.adapter

import androidx.recyclerview.widget.DiffUtil

val commentDetailItemsDiffUtil =
    object : DiffUtil.ItemCallback<CommentDetailItems>() {
        override fun areItemsTheSame(
            oldItem: CommentDetailItems,
            newItem: CommentDetailItems,
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: CommentDetailItems,
            newItem: CommentDetailItems,
        ): Boolean = oldItem == newItem
    }
