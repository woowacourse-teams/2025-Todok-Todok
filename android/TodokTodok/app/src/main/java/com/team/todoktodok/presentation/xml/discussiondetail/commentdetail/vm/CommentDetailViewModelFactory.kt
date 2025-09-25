package com.team.todoktodok.presentation.xml.discussiondetail.commentdetail.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.ReplyRepository
import com.team.domain.repository.TokenRepository

class CommentDetailViewModelFactory(
    private val commentRepository: CommentRepository,
    private val replyRepository: ReplyRepository,
    private val tokenRepository: TokenRepository,
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
                replyRepository,
                tokenRepository,
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
