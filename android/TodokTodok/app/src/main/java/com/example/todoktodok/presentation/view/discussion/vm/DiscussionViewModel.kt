package com.example.todoktodok.presentation.view.discussion.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.DiscussionRoom
import com.example.domain.repository.DiscussionRoomRepository

class DiscussionViewModel(
    private val discussionRoomRepository: DiscussionRoomRepository,
) : ViewModel() {
    private val _discussionRooms = MutableLiveData<List<DiscussionRoom>>(emptyList())
    val discussionRooms: LiveData<List<DiscussionRoom>> = _discussionRooms

    init {
        loadDiscussionRooms()
    }

    private fun loadDiscussionRooms() {
        _discussionRooms.value = discussionRoomRepository.getDiscussionRooms()
    }
}
