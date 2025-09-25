package com.team.domain.model.notification

sealed class NotificationType {
    data object Like : NotificationType()

    data object Comment : NotificationType()

    data object Reply : NotificationType()
}
