package com.team.todoktodok.presentation.view.discussiondetail.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.team.domain.repository.CommentRepository

class CommentCreateViewModelFactory(
    private val commentRepository: CommentRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val savedStateHandle = extras.createSavedStateHandle()
        if (modelClass.isAssignableFrom(com.team.todoktodok.presentation.view.discussiondetail.vm.CommentCreateViewModel::class.java)) {
            return com.team.todoktodok.presentation.view.discussiondetail.vm.CommentCreateViewModel(
                savedStateHandle,
                commentRepository,
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
