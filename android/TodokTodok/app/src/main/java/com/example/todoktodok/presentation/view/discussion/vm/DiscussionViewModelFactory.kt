package com.example.todoktodok.presentation.view.discussion.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.repository.DiscussionRoomRepository
import com.example.todoktodok.presentation.view.library.vm.LibraryViewModel

class DiscussionViewModelFactory(
    private val discussionRoomRepository: DiscussionRoomRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            return DiscussionViewModel(discussionRoomRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}