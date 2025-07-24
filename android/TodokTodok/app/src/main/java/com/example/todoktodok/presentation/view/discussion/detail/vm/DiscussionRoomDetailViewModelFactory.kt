package com.example.todoktodok.presentation.view.discussion.detail.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.domain.repository.CommentRepository
import com.example.domain.repository.DiscussionRoomRepository

class DiscussionRoomDetailViewModelFactory(
    private val discussionRoomRepository: DiscussionRoomRepository,
    private val commentRepository: CommentRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val savedStateHandle = extras.createSavedStateHandle()
        if (modelClass.isAssignableFrom(DiscussionRoomDetailViewModel::class.java)) {
            return DiscussionRoomDetailViewModel(
                savedStateHandle,
                discussionRoomRepository,
                commentRepository,
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
