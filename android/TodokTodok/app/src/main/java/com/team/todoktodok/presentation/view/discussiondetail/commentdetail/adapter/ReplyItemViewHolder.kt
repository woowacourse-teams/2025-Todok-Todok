package com.team.todoktodok.presentation.view.discussiondetail.commentdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.Reply
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ItemReplyBinding
import com.team.todoktodok.presentation.core.ext.formatWithResource

class ReplyItemViewHolder private constructor(
    private val binding: ItemReplyBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(reply: Reply) {
        with(binding) {
            tvUserNickname.text = reply.user.nickname.value
            tvReplyCreateAt.text =
                reply.createdAt.formatWithResource(root.context, R.string.date_format_pattern)
            tvReplyOpinion.text = reply.content
            ivLike.isSelected = reply.isLikedByMe
            ivLike.setOnClickListener { handler.onReplyLikeClick(reply.replyId) }
            tvLikeCount.text = reply.likeCount.toString()
            tvUserNickname.setOnClickListener {
                handler.onReplyUserNameClick(reply.user.id)
            }
        }
    }

    companion object {
        fun ReplyItemViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): ReplyItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemReplyBinding.inflate(layoutInflater, parent, false)
            return ReplyItemViewHolder(binding, handler)
        }
    }

    interface Handler {
        fun onReplyLikeClick(replyId: Long)

        fun onReplyUserNameClick(userId: Long)
    }
}
