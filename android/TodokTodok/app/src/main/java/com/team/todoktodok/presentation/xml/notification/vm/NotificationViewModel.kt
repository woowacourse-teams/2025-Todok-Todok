package com.team.todoktodok.presentation.xml.notification.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.repository.NotificationRepository
import com.team.todoktodok.presentation.xml.notification.NotificationUiState
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val notificationRepository: NotificationRepository,
) : ViewModel() {
    private val _uiState: MutableLiveData<NotificationUiState> =
        MutableLiveData(NotificationUiState())
    val uiState: LiveData<NotificationUiState> get() = _uiState

    init {
        initNotifications()
    }

    private fun initNotifications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true)
            notificationRepository
                .getNotifications()
                .onSuccess { result ->
                    val notificationCount: Int = result.first
                    val notifications = result.second
                    _uiState.value =
                        _uiState.value?.copy(
                            isLoading = false,
                            notificationCount = notificationCount,
                            notifications = notifications,
                        )
                }.onFailure {
                }
        }
    }
}
