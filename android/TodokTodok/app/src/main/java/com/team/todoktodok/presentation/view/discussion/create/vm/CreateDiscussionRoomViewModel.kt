package com.team.todoktodok.presentation.view.discussion.create.vm

import androidx.lifecycle.ViewModel
import com.team.domain.repository.BookRepository
import com.team.domain.repository.DiscussionRepository

class CreateDiscussionRoomViewModel(
    private val bookRepository: BookRepository,
    private val discussionRepository: DiscussionRepository,
) : ViewModel() {
}