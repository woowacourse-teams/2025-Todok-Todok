package com.team.todoktodok.presentation.view.notification.vm

import androidx.lifecycle.ViewModel
import com.team.domain.repository.NotificationRepository

class NotificationViewModel(
    private val notificationRepository: NotificationRepository,
) : ViewModel()
