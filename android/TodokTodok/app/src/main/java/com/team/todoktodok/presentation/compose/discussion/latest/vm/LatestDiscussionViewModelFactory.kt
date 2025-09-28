package com.team.todoktodok.presentation.compose.discussion.latest.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.ConnectivityObserver
import com.team.domain.repository.DiscussionRepository

class LatestDiscussionViewModelFactory(
    private val discussionRepository: DiscussionRepository,
    private val networkConnectivityObserver: ConnectivityObserver,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LatestDiscussionViewModel::class.java)) {
            return LatestDiscussionViewModel(
                discussionRepository,
                networkConnectivityObserver,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
