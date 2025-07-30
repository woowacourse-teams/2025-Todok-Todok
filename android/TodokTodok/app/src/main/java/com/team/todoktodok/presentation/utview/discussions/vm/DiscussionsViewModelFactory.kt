package com.team.todoktodok.presentation.utview.discussions.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.DiscussionRepository

class DiscussionsViewModelFactory(
    private val discussionRepository: DiscussionRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscussionsViewModel::class.java)) {
            return DiscussionsViewModel(discussionRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
