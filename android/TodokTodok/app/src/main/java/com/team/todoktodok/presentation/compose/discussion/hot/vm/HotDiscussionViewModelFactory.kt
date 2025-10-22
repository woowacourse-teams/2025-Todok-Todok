package com.team.todoktodok.presentation.compose.discussion.hot.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.todoktodok.AppContainer

class HotDiscussionViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HotDiscussionViewModel::class.java)) {
            val repositoryModule = appContainer.repositoryModule
            return HotDiscussionViewModel(
                repositoryModule.discussionRepository,
                appContainer.connectivityObserver,
            ) as T
        }
        return super.create(modelClass)
    }
}
