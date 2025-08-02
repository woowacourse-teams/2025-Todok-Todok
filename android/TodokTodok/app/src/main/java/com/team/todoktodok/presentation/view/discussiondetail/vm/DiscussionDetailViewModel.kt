package com.team.todoktodok.presentation.view.discussiondetail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Comment
import com.team.domain.model.Discussion
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailUiEvent
import kotlinx.coroutines.launch

class DiscussionDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val discussionRepository: DiscussionRepository,
) : ViewModel() {
    val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

    val isMyDiscussion = true
    private val _discussion = MutableLiveData<Discussion>()
    val discussion: LiveData<Discussion> = _discussion

    private val _comments = MutableLiveData<List<Comment>>(emptyList())
    val comments: LiveData<List<Comment>> = _comments

    private val _uiEvent = MutableSingleLiveData<DiscussionDetailUiEvent>()
    val uiEvent: SingleLiveData<DiscussionDetailUiEvent> = _uiEvent

    init {
        viewModelScope.launch {
            loadDiscussionRoom()
        }
    }

    fun showBottomSheet() {
        _uiEvent.setValue(DiscussionDetailUiEvent.ShowComments)
    }

    fun onBackPressed() {
        onUiEvent(DiscussionDetailUiEvent.NavigateUp)
    }

    fun reportDiscussion() {
        _uiEvent.setValue(DiscussionDetailUiEvent.ReportDiscussion(discussionId))
    }

    fun updateDiscussion() {
        _uiEvent.setValue(DiscussionDetailUiEvent.UpdateDiscussion(discussionId))
    }

    fun deleteDiscussion() {
        _uiEvent.setValue(DiscussionDetailUiEvent.DeleteDiscussion(discussionId))
    }

    fun toggleLike() {
        _uiEvent.setValue(DiscussionDetailUiEvent.ToggleLikeOnDiscussion(discussionId))
    }

    private suspend fun loadDiscussionRoom() {
        _discussion.value = discussionRepository.getDiscussion(discussionId).getOrNull()
    }

    private fun onUiEvent(uiEvent: DiscussionDetailUiEvent) {
        _uiEvent.setValue(uiEvent)
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussionId"
    }
}
