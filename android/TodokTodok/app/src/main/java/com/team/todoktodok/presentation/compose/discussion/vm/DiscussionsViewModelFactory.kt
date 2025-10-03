package com.team.todoktodok.presentation.compose.discussion.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.todoktodok.AppContainer

class DiscussionsViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscussionsViewModel::class.java)) {
            val repositoryModule = appContainer.repositoryModule
            val discussionRepository = repositoryModule.discussionRepository
            val memberRepository = repositoryModule.memberRepository
            val notificationRepository = repositoryModule.notificationRepository
            val networkConnectivityObserver = appContainer.connectivityObserver

            return DiscussionsViewModel(
                discussionRepository,
                memberRepository,
                notificationRepository,
                networkConnectivityObserver,
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
