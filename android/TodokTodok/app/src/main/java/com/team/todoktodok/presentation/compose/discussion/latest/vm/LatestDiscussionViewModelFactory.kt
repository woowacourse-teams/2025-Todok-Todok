package com.team.todoktodok.presentation.compose.discussion.latest.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.todoktodok.AppContainer

class LatestDiscussionViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LatestDiscussionViewModel::class.java)) {
            val repositoryModule = appContainer.repositoryModule
            val discussionRepository = repositoryModule.discussionRepository
            val networkConnectivityObserver = appContainer.connectivityObserver
            return LatestDiscussionViewModel(
                discussionRepository,
                networkConnectivityObserver,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
