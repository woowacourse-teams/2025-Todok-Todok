package com.example.todoktodok.presentation.view.discussion.discussions.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.repository.DiscussionRepository

class DiscussionViewModelFactory(
    private val discussionRepository: DiscussionRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscussionViewModel::class.java)) {
            return DiscussionViewModel(discussionRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
