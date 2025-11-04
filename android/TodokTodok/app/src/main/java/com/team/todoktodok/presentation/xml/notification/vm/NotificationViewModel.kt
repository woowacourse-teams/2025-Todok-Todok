package com.team.todoktodok.presentation.xml.notification.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.core.event.MutableSingleLiveData
import com.team.core.event.SingleLiveData
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.repository.NotificationRepository
import com.team.todoktodok.presentation.xml.notification.NotificationUiEvent
import com.team.todoktodok.presentation.xml.notification.NotificationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel
    @Inject
    constructor(
        private val notificationRepository: NotificationRepository,
    ) : ViewModel() {
        private val _uiState: MutableLiveData<NotificationUiState> =
            MutableLiveData(NotificationUiState())
        val uiState: LiveData<NotificationUiState> get() = _uiState

        private val _uiEvent: MutableSingleLiveData<NotificationUiEvent> = MutableSingleLiveData()
        val uiEvent: SingleLiveData<NotificationUiEvent> get() = _uiEvent

        init {
            initNotifications()
        }

        fun deleteNotification(position: Int) {
            val notification = _uiState.value?.notification(position - 1) ?: return
            viewModelScope.launch {
                notificationRepository
                    .deleteNotification(notification.id)
                    .onSuccess {
                        _uiState.value = _uiState.value?.deleteNotification(position - 1)
                    }.onFailure { exception ->
                        _uiEvent.setValue(NotificationUiEvent.ShowException(exception))
                    }
            }
        }

        fun initNotifications() {
            _uiState.value = _uiState.value?.copy(isLoading = true)
            viewModelScope.launch {
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
                    }.onFailure { exception ->
                        _uiEvent.setValue(NotificationUiEvent.ShowException(exception))
                    }
            }
        }

        fun updateUnReadStatus(position: Int) {
            val readNotification = _uiState.value?.notification(position) ?: return
            viewModelScope.launch {
                notificationRepository
                    .readNotification(readNotification.id)
                    .onSuccess {
                        _uiEvent.setValue(NotificationUiEvent.NavigateToDiscussionRoom(readNotification.notificationContent.discussionId))
                    }.onFailure { exceptions ->
                        _uiEvent.setValue(NotificationUiEvent.ShowException(exceptions))
                    }
            }
        }
    }
