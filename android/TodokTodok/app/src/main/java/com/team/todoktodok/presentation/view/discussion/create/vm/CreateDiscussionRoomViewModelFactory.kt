package com.team.todoktodok.presentation.view.discussion.create.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.BookRepository
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.view.discussion.create.SerializationCreateDiscussionRoomMode

class CreateDiscussionRoomViewModelFactory(
    private val mode: SerializationCreateDiscussionRoomMode,
    private val bookRepository: BookRepository,
    private val discussionRepository: DiscussionRepository,
    private val tokenRepository: TokenRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateDiscussionRoomViewModel::class.java)) {
            return CreateDiscussionRoomViewModel(
                mode,
                bookRepository,
                discussionRepository,
                tokenRepository,
            ) as T
        } else {
            throw IllegalArgumentException("알 수 없는 ViewModel 클래스입니다: ${modelClass.name}")
        }
    }
}
