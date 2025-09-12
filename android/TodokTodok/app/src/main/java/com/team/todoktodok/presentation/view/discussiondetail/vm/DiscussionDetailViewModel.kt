package com.team.todoktodok.presentation.view.discussiondetail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.exception.NetworkResult
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussion.create.SerializationCreateDiscussionRoomMode
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailUiEvent
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DiscussionDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val discussionRepository: DiscussionRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    private val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

    var mode = savedStateHandle.get<SerializationCreateDiscussionRoomMode>(KEY_MODE)
        private set
    private var coalesceJob: Job? = null
    private val _uiState = MutableLiveData<DiscussionDetailUiState>()
    val uiState: LiveData<DiscussionDetailUiState> = _uiState

    private val _uiEvent = MutableSingleLiveData<DiscussionDetailUiEvent>()
    val uiEvent: SingleLiveData<DiscussionDetailUiEvent> = _uiEvent

    init {
        viewModelScope.launch {
            loadDiscussionRoom()
        }
    }

    fun onFinishEvent() {
        val currentState = _uiState.value ?: return
        onUiEvent(
            DiscussionDetailUiEvent.NavigateToDiscussionsWithResult(
                mode,
                currentState.discussion.id,
            ),
        )
    }

    fun fetchMode(mode: SerializationCreateDiscussionRoomMode) {
        this.mode = mode
        savedStateHandle[KEY_MODE] = mode
    }

    fun showComments() {
        onUiEvent(DiscussionDetailUiEvent.ShowComments(discussionId))
    }

    fun reportDiscussion(reason: String) {
        viewModelScope.launch {
            handleResult(discussionRepository.reportDiscussion(discussionId, reason)) {
                onUiEvent(DiscussionDetailUiEvent.ShowReportDiscussionSuccessMessage)
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
            handleResult(discussionRepository.deleteDiscussion(discussionId)) {
                onUiEvent(
                    DiscussionDetailUiEvent.DeleteDiscussion(
                        discussionId,
                    ),
                )
            }
        }
    }

    fun toggleLike() {
        val currentUiState = _uiState.value ?: return
        val currentDiscussion = currentUiState.discussion
        val desiredIsLikedByMe = !currentDiscussion.isLikedByMe
        val likeCountDelta = if (desiredIsLikedByMe) 1 else -1
        _uiState.value =
            currentUiState.copy(
                discussion =
                    currentUiState.discussion.copy(
                        isLikedByMe = desiredIsLikedByMe,
                        likeCount =
                            (currentUiState.discussion.likeCount + likeCountDelta).coerceAtLeast(
                                0,
                            ),
                    ),
            )
        coalesceJob?.cancel()
        coalesceJob =
            viewModelScope.launch {
                delay(250)
                val isToggle =
                    currentDiscussion.likeCount != _uiState.value?.discussion?.likeCount
                if (!isToggle) return@launch
                handleResult(discussionRepository.toggleLike(discussionId)) {
                    loadDiscussionRoom()
                }
            }
    }

    fun navigateToProfile() {
        val memberId =
            _uiState.value
                ?.discussion
                ?.writer
                ?.id ?: return
        _uiEvent.setValue(DiscussionDetailUiEvent.NavigateToProfile(memberId))
    }

    private suspend fun loadDiscussionRoom() {
        handleResult(discussionRepository.getDiscussion(discussionId)) { discussion ->
            _uiState.value =
                DiscussionDetailUiState(
                    discussion = discussion,
                    isMyDiscussion = discussion.writer.id == tokenRepository.getMemberId(),
                    isLoading = false,
                )
        }
    }

    private inline fun <T> handleResult(
        result: NetworkResult<T>,
        onFailure: () -> Unit = {},
        onSuccess: (T) -> Unit = {},
    ) {
        when (result) {
            is NetworkResult.Success -> onSuccess(result.data)
            is NetworkResult.Failure -> {
                onFailure()
                onUiEvent(
                    DiscussionDetailUiEvent.ShowErrorMessage(result.exception),
                )
                _uiState.value = _uiState.value?.copy(isLoading = false)
            }
        }
    }

    private fun onUiEvent(uiEvent: DiscussionDetailUiEvent) {
        _uiEvent.setValue(uiEvent)
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussionId"
        const val KEY_MODE = "mode"
    }
}
