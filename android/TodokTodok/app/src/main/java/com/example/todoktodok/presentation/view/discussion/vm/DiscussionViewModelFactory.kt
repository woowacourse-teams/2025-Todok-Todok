package com.example.todoktodok.presentation.view.discussion.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.repository.DiscussionRoomRepository

class DiscussionViewModelFactory(
    private val discussionRoomRepository: DiscussionRoomRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscussionViewModel::class.java)) {
            return DiscussionViewModel(discussionRoomRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
