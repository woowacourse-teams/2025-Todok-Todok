package com.team.todoktodok.presentation.compose.bookdiscussions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.team.todoktodok.AppContainer

class BookDiscussionsViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val module = appContainer.repositoryModule
        return BookDiscussionsViewModel(
            extras.createSavedStateHandle(),
            module.bookRepository,
        ) as T
    }
}
