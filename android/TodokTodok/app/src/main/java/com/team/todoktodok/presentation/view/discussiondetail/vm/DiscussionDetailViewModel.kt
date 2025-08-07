package com.team.todoktodok.presentation.view.discussiondetail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailUiEvent
import com.team.todoktodok.presentation.view.discussiondetail.model.DiscussionUiModel
import kotlinx.coroutines.launch

class DiscussionDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val discussionRepository: DiscussionRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    private val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

    private val _discussion = MutableLiveData<DiscussionUiModel>()
    val discussion: LiveData<DiscussionUiModel> = _discussion

    private val _uiEvent = MutableSingleLiveData<DiscussionDetailUiEvent>()
    val uiEvent: SingleLiveData<DiscussionDetailUiEvent> = _uiEvent

    init {
        viewModelScope.launch {
            loadDiscussionRoom()
        }
    }

    fun showComments() {
        onUiEvent(DiscussionDetailUiEvent.ShowComments(discussionId))
    }

    fun reportDiscussion() {
        viewModelScope.launch {
            runCatching {
                discussionRepository.reportDiscussion(discussionId)
            }.onFailure {
                onUiEvent(DiscussionDetailUiEvent.AlreadyReportDiscussion)
            }
        }
    }

    fun updateDiscussion() {
        onUiEvent(DiscussionDetailUiEvent.UpdateDiscussion(discussionId))
    }

    fun deleteDiscussion() {
        onUiEvent(DiscussionDetailUiEvent.DeleteDiscussion(discussionId))
    }

    fun toggleLike() {
        onUiEvent(DiscussionDetailUiEvent.ToggleLikeOnDiscussion(discussionId))
    }

    private suspend fun loadDiscussionRoom() {
        val discussion = discussionRepository.getDiscussion(discussionId).getOrThrow()
        _discussion.value =
            DiscussionUiModel(
                discussion,
                discussion.writer.id == tokenRepository.getMemberId(),
            )
        discussionRepository.getDiscussion(discussionId).getOrNull()
    }

    private fun onUiEvent(uiEvent: DiscussionDetailUiEvent) {
        _uiEvent.setValue(uiEvent)
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussionId"
    }
}
