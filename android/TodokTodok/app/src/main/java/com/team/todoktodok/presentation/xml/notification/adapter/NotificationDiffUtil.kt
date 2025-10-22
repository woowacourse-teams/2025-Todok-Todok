package com.team.todoktodok.presentation.xml.notification.adapter

import androidx.recyclerview.widget.DiffUtil

class NotificationDiffUtil : DiffUtil.ItemCallback<NotificationGroup>() {
    override fun areItemsTheSame(
        oldItem: NotificationGroup,
        newItem: NotificationGroup,
    ): Boolean =
        when {
            oldItem is NotificationGroup.Information && newItem is NotificationGroup.Information -> true
            oldItem is NotificationGroup.Count && newItem is NotificationGroup.Count -> true
            oldItem is NotificationGroup.Notification && newItem is NotificationGroup.Notification ->
                oldItem.notification.id ==
                    newItem.notification.id

            else -> false
        }

    override fun areContentsTheSame(
        oldItem: NotificationGroup,
        newItem: NotificationGroup,
    ): Boolean =
        when {
            oldItem is NotificationGroup.Information && newItem is NotificationGroup.Information -> true
            oldItem is NotificationGroup.Count && newItem is NotificationGroup.Count -> oldItem == newItem
            oldItem is NotificationGroup.Notification && newItem is NotificationGroup.Notification ->
                oldItem.notification ==
                    newItem.notification

            else -> false
        }
}
