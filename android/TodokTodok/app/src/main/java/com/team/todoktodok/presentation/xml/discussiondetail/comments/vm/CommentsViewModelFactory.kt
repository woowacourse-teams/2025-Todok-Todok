package com.team.todoktodok.presentation.xml.discussiondetail.comments.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.TokenRepository

class CommentsViewModelFactory(
    private val commentRepository: CommentRepository,
    private val tokenRepository: TokenRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val savedStateHandle = extras.createSavedStateHandle()
        if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
            return CommentsViewModel(
                savedStateHandle,
                commentRepository,
                tokenRepository,
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
