package com.team.todoktodok.presentation.view.discussiondetail.commentdetail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.adapter.CommentDetailItems.ViewType.Companion.ViewType
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.adapter.CommentItemViewHolder.Companion.CommentItemViewHolder
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.adapter.ReplyItemViewHolder.Companion.ReplyItemViewHolder

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
        when (ViewType(viewType)) {
            CommentDetailItems.ViewType.COMMENT_ITEM -> CommentItemViewHolder(parent, handler)
            CommentDetailItems.ViewType.REPLIES_ITEM -> ReplyItemViewHolder(parent, handler)
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is CommentDetailItems.CommentItem -> (holder as CommentItemViewHolder).bind(item.value)
            is CommentDetailItems.ReplyItem -> (holder as ReplyItemViewHolder).bind(item.value)
        }
    }

    interface Handler :
        ReplyItemViewHolder.Handler,
        CommentItemViewHolder.Handler
}
