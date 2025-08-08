package com.team.todoktodok.presentation.view.discussiondetail.replycreate.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.team.domain.repository.ReplyRepository

class ReplyCreateViewModelFactory(
    private val replyRepository: ReplyRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val savedStateHandle = extras.createSavedStateHandle()
        if (modelClass.isAssignableFrom(ReplyCreateViewModel::class.java)) {
            return ReplyCreateViewModel(
                savedStateHandle,
                replyRepository,
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
