package com.team.todoktodok.presentation.compose.main.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.todoktodok.AppContainer

class MainViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val repositoryModule = appContainer.repositoryModule
            val discussionRepository = repositoryModule.discussionRepository
            val notificationRepository = repositoryModule.notificationRepository
            val networkConnectivityObserver = appContainer.connectivityObserver

            return MainViewModel(
                discussionRepository,
                notificationRepository,
                networkConnectivityObserver,
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
