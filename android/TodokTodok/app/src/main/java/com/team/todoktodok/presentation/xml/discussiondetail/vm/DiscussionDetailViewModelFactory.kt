package com.team.todoktodok.presentation.xml.discussiondetail.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.TokenRepository

class DiscussionDetailViewModelFactory(
    private val discussionRepository: DiscussionRepository,
    private val tokenRepository: TokenRepository,
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
                tokenRepository,
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
