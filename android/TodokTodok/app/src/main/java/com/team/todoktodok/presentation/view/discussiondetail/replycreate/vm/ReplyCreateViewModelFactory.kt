package com.team.todoktodok.presentation.view.discussiondetail.replycreate.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.team.domain.repository.CommentRepository

class ReplyCreateViewModelFactory(
    private val commentRepository: CommentRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val savedStateHandle = extras.createSavedStateHandle()
        if (modelClass.isAssignableFrom(ReplyCreateViewModel::class.java)) {
            return ReplyCreateViewModel(
                savedStateHandle,
                commentRepository,
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
