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
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(reply: Reply) {
        with(binding) {
            tvUserNickname.text = reply.user.nickname.value
            tvReplyCreateAt.text =
                reply.createdAt.formatWithResource(root.context, R.string.date_format_pattern)
            tvReplyOpinion.text = reply.content
        }
    }

    companion object {
        fun ReplyItemViewHolder(parent: ViewGroup): ReplyItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemReplyBinding.inflate(layoutInflater, parent, false)
            return ReplyItemViewHolder(binding)
        }
    }
}
