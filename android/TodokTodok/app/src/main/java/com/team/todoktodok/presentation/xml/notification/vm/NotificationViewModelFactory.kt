package com.team.todoktodok.presentation.xml.notification.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.NotificationRepository

class NotificationViewModelFactory(
    private val notificationRepository: NotificationRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(notificationRepository) as T
        } else {
            throw IllegalArgumentException("알 수 없는 ViewModel 클래스입니다: ${modelClass.name}")
        }
    }
}
