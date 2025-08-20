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
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailUiEvent
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailUiState
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailUiState.Companion.INIT_DISCUSSION_DETAIL_UI_STATE
import com.team.todoktodok.presentation.view.discussiondetail.model.DiscussionItemUiState
import kotlinx.coroutines.launch

class DiscussionDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val discussionRepository: DiscussionRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    private val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

    private val _uiState = MutableLiveData(INIT_DISCUSSION_DETAIL_UI_STATE.copy(isLoading = true))
    val uiState: LiveData<DiscussionDetailUiState> = _uiState

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
            handleResult(discussionRepository.reportDiscussion(discussionId)) {
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
        viewModelScope.launch {
            handleResult(discussionRepository.toggleLike(discussionId)) {
                loadDiscussionRoom()
            }
        }
    }

    fun navigateToProfile() {
        val memberId =
            _uiState.value
                ?.discussionItemUiState
                ?.discussion
                ?.writer
                ?.id ?: return
        _uiEvent.setValue(DiscussionDetailUiEvent.NavigateToProfile(memberId))
    }

    private suspend fun loadDiscussionRoom() {
        handleResult(discussionRepository.getDiscussion(discussionId)) { discussion ->
            _uiState.value =
                _uiState.value?.copy(
                    discussionItemUiState =
                        DiscussionItemUiState(
                            discussion,
                            discussionId == tokenRepository.getMemberId(),
                        ),
                    isLoading = false,
                )
        }
    }

    private inline fun <T> handleResult(
        result: NetworkResult<T>,
        onSuccess: (T) -> Unit,
    ) {
        when (result) {
            is NetworkResult.Success -> onSuccess(result.data)
            is NetworkResult.Failure ->
                onUiEvent(
                    DiscussionDetailUiEvent.ShowErrorMessage(result.exception),
                )
        }
    }

    private fun onUiEvent(uiEvent: DiscussionDetailUiEvent) {
        _uiEvent.setValue(uiEvent)
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussionId"
    }
}
