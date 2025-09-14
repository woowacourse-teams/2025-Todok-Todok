package com.team.todoktodok.presentation.view.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.notification.Notification
import com.team.domain.model.notification.NotificationType
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ItemNotificationBinding
import com.team.todoktodok.presentation.core.ext.formatKorean

class NotificationViewHolder(
    private val binding: ItemNotificationBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(notification: Notification) {
        binding.apply {
            tvNickname.text = notification.nickname.value
            tvDiscussionTitle.text = notification.discussionTitle
            tvContent.text = notification.content
            tvDate.text = notification.receivedAt.formatKorean()
        }
        when (notification.type) {
            is NotificationType.Like -> {
                binding.apply {
                    tvType.text = "좋아요"
                    tvContent.isVisible = false
                    ivIcon.setImageResource(R.drawable.ic_heart)
                }
            }

            is NotificationType.Comment -> {
                binding.apply {
                    tvType.text = "댓글"
                    ivIcon.setImageResource(R.drawable.ic_comment)
                }
            }

            is NotificationType.Reply -> {
                binding.apply {
                    tvType.text = "대댓글"
                    ivIcon.setImageResource(R.drawable.ic_reply)
                }
            }
        }
    }

    companion object {
        fun NotificationViewHolder(parent: ViewGroup): NotificationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemNotificationBinding.inflate(layoutInflater, parent, false)
            return NotificationViewHolder(binding)
        }
    }
}
