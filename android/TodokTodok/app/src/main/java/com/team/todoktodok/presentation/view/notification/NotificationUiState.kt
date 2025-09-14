package com.team.todoktodok.presentation.view.notification

import com.team.domain.model.notification.Notification
import com.team.todoktodok.presentation.view.notification.adapter.NotificationGroup

data class NotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
) {
    val notificationGroup: List<NotificationGroup> =
        listOf(
            *notifications.map { NotificationGroup.Notification(it) }.toTypedArray(),
            NotificationGroup.Information,
        )
}
