package com.team.ui_xml.discussiondetail.commentdetail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CommentDetailAdapter(
    private val handler: Handler,
) : ListAdapter<CommentDetailItems, RecyclerView.ViewHolder>(
        commentDetailItemsDiffUtil,
    ) {
    override fun getItemViewType(position: Int): Int = getItem(position).viewType.sequence

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (CommentDetailItems.ViewType.Companion.ViewType(viewType)) {
            CommentDetailItems.ViewType.COMMENT_ITEM ->
                CommentItemViewHolder.Companion.CommentItemViewHolder(
                    parent,
                    handler,
                )
            CommentDetailItems.ViewType.REPLIES_ITEM ->
                ReplyItemViewHolder.Companion.ReplyItemViewHolder(
                    parent,
                    handler,
                )
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is CommentDetailItems.CommentItem -> (holder as CommentItemViewHolder).bind(item)
            is CommentDetailItems.ReplyItem -> (holder as ReplyItemViewHolder).bind(item)
        }
    }

    interface Handler :
        ReplyItemViewHolder.Handler,
        CommentItemViewHolder.Handler
}
