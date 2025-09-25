package com.team.todoktodok.presentation.xml.notification

import com.team.domain.model.notification.Notification
import com.team.todoktodok.presentation.xml.notification.adapter.NotificationGroup

data class NotificationUiState(
    val isLoading: Boolean = false,
    val notificationCount: Int = 0,
    val notifications: List<Notification> = emptyList(),
) {
    fun notification(position: Int): Notification = notifications[position]

    val notificationGroup: List<NotificationGroup> =
        listOf(
            NotificationGroup.Count(notificationCount),
            *notifications.sortedByDescending { it.createdAt }
                .map { NotificationGroup.Notification(it) }
                .toTypedArray(),
            NotificationGroup.Information,
        )

    fun deleteNotification(position: Int): NotificationUiState {
        val notification = notification(position = position)
        val minus = if (notification.isRead) 0 else 1
        return NotificationUiState(
            isLoading = false,
            notificationCount = this.notificationCount - minus,
            notifications = notifications.filterIndexed { index, notification -> index != position }
        )
    }
}
