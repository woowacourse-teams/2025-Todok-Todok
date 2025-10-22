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

class NotificationViewHolder private constructor(
    private val binding: ItemNotificationBinding,
    private val updateUnreadStatus: UpdateUnreadStatusClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            val position = bindingAdapterPosition
            if (position == RecyclerView.NO_POSITION) return@setOnClickListener
            updateUnreadStatus.onClick(bindingAdapterPosition - NOTIFICATION_COUNT_SIZE)
        }
    }

    fun bind(notification: Notification) {
        binding.apply {
            viewUnread.visibility = if (notification.isRead) View.GONE else View.VISIBLE
            tvContent.text = notification.notificationContent.content
            tvDate.text = notification.createdAt.formatKorean()
        }
        when (notification.notificationContent.type) {
            is NotificationType.Like -> {
                binding.apply {
                    tvType.text = itemView.context.getString(R.string.notification_type_like)
                    tvContent.isVisible = false
                    ivIcon.setImageResource(R.drawable.ic_heart)
                    tvNickname.text =
                        binding.root.context.getString(
                            R.string.notification_like,
                            notification.notificationContent.nickname.value,
                            notification.notificationContent.discussionTitle,
                        )
                }
            }

            is NotificationType.Comment -> {
                binding.apply {
                    tvType.text = itemView.context.getString(R.string.notification_type_comment)
                    ivIcon.setImageResource(R.drawable.ic_comment)
                    tvNickname.text =
                        binding.root.context.getString(
                            R.string.notification_comment,
                            notification.notificationContent.nickname.value,
                            notification.notificationContent.discussionTitle,
                        )
                }
            }

            is NotificationType.Reply -> {
                binding.apply {
                    tvType.text = itemView.context.getString(R.string.notification_type_reply)
                    ivIcon.setImageResource(R.drawable.ic_reply)
                    tvNickname.text =
                        binding.root.context.getString(
                            R.string.notification_comment,
                            notification.notificationContent.nickname.value,
                            notification.notificationContent.discussionTitle,
                        )
                }
            }
        }
    }

    companion object {
        private const val NOTIFICATION_COUNT_SIZE: Int = 1

        fun NotificationViewHolder(
            parent: ViewGroup,
            updateUnreadStatus: UpdateUnreadStatusClickListener,
        ): NotificationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemNotificationBinding.inflate(layoutInflater, parent, false)
            return NotificationViewHolder(binding, updateUnreadStatus)
        }
    }

    fun interface UpdateUnreadStatusClickListener {
        fun onClick(position: Int)
    }
}
