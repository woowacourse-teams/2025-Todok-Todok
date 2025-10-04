package com.team.todoktodok.presentation.compose.my.books.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.todoktodok.AppContainer

class MyBooksViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyBooksViewModel::class.java)) {
            val repositoryModule = appContainer.repositoryModule
            return MyBooksViewModel(
                repositoryModule.memberRepository,
                appContainer.connectivityObserver,
            ) as T
        }
        return super.create(modelClass)
    }
}
