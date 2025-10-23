package com.team.todoktodok.presentation.xml.discussiondetail.commentdetail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemReplyBinding
import com.team.todoktodok.presentation.core.ext.loadCircleImage
import com.team.todoktodok.presentation.core.ext.toRelativeString

class ReplyItemViewHolder private constructor(
    private val binding: ItemReplyBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(replyItem: CommentDetailItems.ReplyItem) {
        val reply = replyItem.value.reply
        with(binding) {
            tvUserNickname.text = reply.writer.nickname
            tvReplyCreateAt.text =
                reply.createdAt.toRelativeString(
                    root.context,
                )
            tvReplyOpinion.text = reply.content
            ivLike.isSelected = reply.isLikedByMe
            ivLike.setOnClickListener { handler.onClickReplyLike(reply.replyId) }
            tvLikeCount.text = reply.likeCount.toString()
            ivUserProfile.loadCircleImage(replyItem.value.reply.writer.profileImage)
            ivUserProfile.setOnClickListener {
                handler.onClickReplyUser(reply.writer.id, reply.writer.nickname)
            }
            tvUserNickname.setOnClickListener {
                handler.onClickReplyUser(reply.writer.id, reply.writer.nickname)
            }
            ivReplyOption.setOnClickListener {
                handler.onClickReplyOption(replyItem, ivReplyOption)
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
        fun onClickReplyLike(replyId: Long)

        fun onClickReplyUser(
            userId: Long,
            userName: String,
        )

        fun onClickReplyOption(
            item: CommentDetailItems.ReplyItem,
            anchorView: View,
        )
    }
}
