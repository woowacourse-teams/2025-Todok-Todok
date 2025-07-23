package com.example.todoktodok.presentation.view.discussion.discussions.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.DiscussionRoom
import com.example.domain.repository.DiscussionRoomRepository
import com.example.todoktodok.presentation.core.event.MutableSingleLiveData
import com.example.todoktodok.presentation.core.event.SingleLiveData
import com.example.todoktodok.presentation.view.discussion.discussions.DiscussionUiEvent

class DiscussionViewModel(
    private val discussionRoomRepository: DiscussionRoomRepository,
) : ViewModel() {
    private val _discussionRooms = MutableLiveData<List<DiscussionRoom>>()
    val discussionRooms: LiveData<List<DiscussionRoom>> = _discussionRooms

    private val _uiEvent = MutableSingleLiveData<DiscussionUiEvent>()
    val uiEvent: SingleLiveData<DiscussionUiEvent> = _uiEvent

    init {
        loadDiscussionRooms()
    }

    fun onUiEvent(uiEvent: DiscussionUiEvent) {
        _uiEvent.postValue(uiEvent)
    }

    private fun loadDiscussionRooms() {
        _discussionRooms.value = discussionRoomRepository.getDiscussionRooms()
    }
}
