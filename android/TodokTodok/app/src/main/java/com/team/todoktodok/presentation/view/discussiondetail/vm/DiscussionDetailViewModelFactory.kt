package com.team.todoktodok.presentation.view.discussiondetail.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.team.domain.repository.DiscussionRepository

class DiscussionDetailViewModelFactory(
    private val discussionRepository: DiscussionRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val savedStateHandle = extras.createSavedStateHandle()
        if (modelClass.isAssignableFrom(DiscussionDetailViewModel::class.java)) {
            return DiscussionDetailViewModel(
                savedStateHandle,
                discussionRepository,
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
