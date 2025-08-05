package com.team.todoktodok.presentation.view.discussiondetail.commentdetail.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.team.domain.repository.CommentRepository
import com.team.todoktodok.presentation.view.discussiondetail.comments.vm.CommentsViewModel

class CommentDetailViewModelFactory(
    private val commentRepository: CommentRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val savedStateHandle = extras.createSavedStateHandle()
        if (modelClass.isAssignableFrom(CommentDetailViewModel::class.java)) {
            return CommentDetailViewModel(
                savedStateHandle,
                commentRepository,
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
