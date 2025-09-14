package com.team.todoktodok.presentation.view.notification

data class NotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<String> = emptyList(),
)
