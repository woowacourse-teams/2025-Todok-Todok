package com.team.todoktodok.presentation.xml.notification.adapter

import android.view.LayoutInflater
import android.view.View
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
            viewUnread.visibility = if (notification.isRead) View.GONE else View.VISIBLE
            tvNickname.text = notification.notificationContent.nickname.value
            tvNickname.text = notification.notificationContent.discussionTitle
            tvContent.text = notification.notificationContent.content
            tvDate.text = notification.createdAt.formatKorean()
        }
        when (notification.notificationContent.type) {
            is NotificationType.Like -> {
                binding.apply {
                    tvType.text = itemView.context.getString(R.string.notification_type_like)
                    tvContent.isVisible = false
                    ivIcon.setImageResource(R.drawable.ic_heart)
                }
            }

            is NotificationType.Comment -> {
                binding.apply {
                    tvType.text = itemView.context.getString(R.string.notification_type_comment)
                    ivIcon.setImageResource(R.drawable.ic_comment)
                }
            }

            is NotificationType.Reply -> {
                binding.apply {
                    tvType.text = itemView.context.getString(R.string.notification_type_reply)
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
