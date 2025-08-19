package com.team.todoktodok.presentation.view.discussiondetail.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Discussion
import com.team.domain.model.LikeStatus
import com.team.domain.model.exception.NetworkResult
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailUiEvent
import com.team.todoktodok.presentation.view.discussiondetail.model.DiscussionItemUiState
import kotlinx.coroutines.launch

class DiscussionDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val discussionRepository: DiscussionRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    private val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

    private val _discussion = MutableLiveData<DiscussionItemUiState>()
    val discussion: LiveData<DiscussionItemUiState> = _discussion

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
            when (val reportDiscussion = discussionRepository.reportDiscussion(discussionId)) {
                is NetworkResult.Failure ->
                    onUiEvent(DiscussionDetailUiEvent.ShowErrorMessage(reportDiscussion.exception))

                is NetworkResult.Success<Unit> ->
                    onUiEvent(DiscussionDetailUiEvent.ShowReportSuccessMessage)
            }
        }
    }

    fun reloadDiscussion() {
        viewModelScope.launch {
            loadDiscussionRoom()
        }
    }

    fun updateDiscussion() {
        onUiEvent(DiscussionDetailUiEvent.UpdateDiscussion(discussionId))
    }

    fun deleteDiscussion() {
        viewModelScope.launch {
            when (val deleteDiscussion = discussionRepository.deleteDiscussion(discussionId)) {
                is NetworkResult.Failure -> {
                    Log.d("12345", deleteDiscussion.exception.toString())
                    _uiEvent.setValue(
                        DiscussionDetailUiEvent.ShowErrorMessage(
                            deleteDiscussion.exception,
                        ),
                    )
                }

                is NetworkResult.Success<Unit> ->
                    onUiEvent(
                        DiscussionDetailUiEvent.DeleteDiscussion(
                            discussionId,
                        ),
                    )
            }
        }
    }

    fun toggleLike() {
        viewModelScope.launch {
            when (
                val likeStatus = discussionRepository.toggleLike(discussionId)
            ) {
                is NetworkResult.Failure -> {
                    _uiEvent.setValue(DiscussionDetailUiEvent.ShowErrorMessage(likeStatus.exception))
                }

                is NetworkResult.Success<LikeStatus> -> loadDiscussionRoom()
            }
        }
    }

    fun navigateToProfile() {
        val memberId =
            _discussion.value
                ?.discussion
                ?.writer
                ?.id ?: return
        _uiEvent.setValue(DiscussionDetailUiEvent.NavigateToProfile(memberId))
    }

    private suspend fun loadDiscussionRoom() {
        when (val discussion = discussionRepository.getDiscussion(discussionId)) {
            is NetworkResult.Failure -> DiscussionDetailUiEvent.ShowErrorMessage(discussion.exception)
            is NetworkResult.Success<Discussion> -> {
                _discussion.value =
                    DiscussionItemUiState(
                        discussion.data,
                        discussion.data.writer.id == tokenRepository.getMemberId(),
                    )
            }
        }
    }

    private fun onUiEvent(uiEvent: DiscussionDetailUiEvent) {
        _uiEvent.setValue(uiEvent)
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussionId"
    }
}
