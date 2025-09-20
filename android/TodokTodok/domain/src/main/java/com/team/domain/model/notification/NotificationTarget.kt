package com.team.domain.model.notification

sealed interface NotificationTarget {
    data object Discussion : NotificationTarget

    data object Comment : NotificationTarget

    data object Reply : NotificationTarget
}
