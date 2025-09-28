package com.team.todoktodok.presentation.compose.discussion.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.ConnectivityObserver
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.NotificationRepository

class DiscussionsViewModelFactory(
    private val discussionRepository: DiscussionRepository,
    private val notificationRepository: NotificationRepository,
    private val networkConnectivityObserver: ConnectivityObserver,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscussionsViewModel::class.java)) {
            return DiscussionsViewModel(
                discussionRepository,
                notificationRepository,
                networkConnectivityObserver,
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
