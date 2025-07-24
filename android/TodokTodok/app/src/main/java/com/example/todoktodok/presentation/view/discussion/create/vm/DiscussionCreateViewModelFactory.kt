package com.example.todoktodok.presentation.view.discussion.create.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.repository.DiscussionRepository
import com.example.domain.repository.NoteRepository

class DiscussionCreateViewModelFactory(
    private val discussionRepository: DiscussionRepository,
    private val noteRepository: NoteRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscussionCreateViewModel::class.java)) {
            return DiscussionCreateViewModel(discussionRepository, noteRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
