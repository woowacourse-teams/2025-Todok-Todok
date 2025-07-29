package com.team.todoktodok.presentation.utview.discussion.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.BookRepository
import com.team.domain.repository.DiscussionRepository

class CreateDiscussionRoomViewModelFactory(
    private val bookRepository: BookRepository,
    private val discussionRepository: DiscussionRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateDiscussionRoomViewModel::class.java)) {
            return CreateDiscussionRoomViewModel(bookRepository, discussionRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
