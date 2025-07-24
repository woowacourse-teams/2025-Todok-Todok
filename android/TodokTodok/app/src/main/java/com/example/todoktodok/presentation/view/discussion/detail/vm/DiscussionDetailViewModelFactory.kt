package com.example.todoktodok.presentation.view.discussion.detail.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.domain.repository.CommentRepository
import com.example.domain.repository.DiscussionRepository

class DiscussionDetailViewModelFactory(
    private val discussionRepository: DiscussionRepository,
    private val commentRepository: CommentRepository,
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
                commentRepository,
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
