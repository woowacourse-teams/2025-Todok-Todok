package com.team.todoktodok.presentation.compose.discussion.all.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.todoktodok.AppContainer

class AllDiscussionViewModelFactory(
    private val container: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllDiscussionViewModel::class.java)) {
            val repositoryModule = container.repositoryModule
            val discussionRepository = repositoryModule.discussionRepository
            val networkConnectivityObserver = container.connectivityObserver
            return AllDiscussionViewModel(
                discussionRepository = discussionRepository,
                connectivityObserver = networkConnectivityObserver,
            ) as T
        }
        return super.create(modelClass)
    }
}
