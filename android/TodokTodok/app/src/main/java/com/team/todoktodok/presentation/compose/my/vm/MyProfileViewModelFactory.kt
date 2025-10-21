package com.team.todoktodok.presentation.compose.my.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.todoktodok.AppContainer

class MyProfileViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyProfileViewModel::class.java)) {
            val repositoryModule = appContainer.repositoryModule
            return MyProfileViewModel(
                repositoryModule.memberRepository,
                repositoryModule.discussionRepository,
                repositoryModule.tokenRepository,
                appContainer.connectivityObserver,
            ) as T
        }
        throw IllegalArgumentException()
    }
}
