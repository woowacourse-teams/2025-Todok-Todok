package com.team.todoktodok.presentation.xml.discussiondetail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.xml.discussion.create.SerializationCreateDiscussionRoomMode
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailUiEvent
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailUiState
import com.team.todoktodok.presentation.xml.serialization.toSerialization
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DiscussionDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val discussionRepository: DiscussionRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    var discussionId: Long? = savedStateHandle.get<Long>(KEY_DISCUSSION_ID)
        private set

    var mode = savedStateHandle.get<SerializationCreateDiscussionRoomMode>(KEY_MODE)
        private set
    private var coalesceJob: Job? = null
    private val _uiState = MutableLiveData<DiscussionDetailUiState>(DiscussionDetailUiState.Loading)
    val uiState: LiveData<DiscussionDetailUiState> = _uiState

    private val _uiEvent = MutableSingleLiveData<DiscussionDetailUiEvent>()
    val uiEvent: SingleLiveData<DiscussionDetailUiEvent> = _uiEvent

    fun onFinishEvent() {
        val currentState = _uiState.value ?: return
        if (currentState is DiscussionDetailUiState.Success) {
            onUiEvent(
                DiscussionDetailUiEvent.NavigateToDiscussionsWithResult(
                    mode,
                    currentState.discussion.toSerialization(),
                ),
            )
        }
    }

    fun initLoadDiscission(id: Long) {
        discussionId = id
        savedStateHandle[KEY_DISCUSSION_ID] = id
        viewModelScope.launch {
            loadDiscussionRoom()
        }
    }

    fun fetchMode(mode: SerializationCreateDiscussionRoomMode) {
        this.mode = mode
        savedStateHandle[KEY_MODE] = mode
    }

    fun reportDiscussion(reason: String) {
        viewModelScope.launch {
            handleResult(
                discussionRepository.reportDiscussion(
                    discussionId ?: THROW_DISCUSSION_ID,
                    reason,
                ),
            ) {
                onUiEvent(DiscussionDetailUiEvent.ShowReportDiscussionSuccessMessage)
            }
        }
    }

    fun reloadDiscussion() {
        viewModelScope.launch {
            loadDiscussionRoom()
            onUiEvent(DiscussionDetailUiEvent.ReloadedDiscussion)
        }
    }

    fun updateDiscussion() {
        onUiEvent(DiscussionDetailUiEvent.UpdateDiscussion(discussionId ?: THROW_DISCUSSION_ID))
    }

    fun deleteDiscussion() {
        viewModelScope.launch {
            handleResult(
                discussionRepository.deleteDiscussion(
                    discussionId ?: THROW_DISCUSSION_ID,
                ),
            ) {
                onUiEvent(
                    DiscussionDetailUiEvent.DeleteDiscussion(
                        discussionId ?: THROW_DISCUSSION_ID,
                    ),
                )
            }
        }
    }

    fun toggleLike() {
        val currentUiState = _uiState.value ?: return
        if (currentUiState !is DiscussionDetailUiState.Success) return
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
                val currentUiState = _uiState.value ?: return@launch
                if (currentUiState !is DiscussionDetailUiState.Success) return@launch
                val isToggle =
                    currentDiscussion.likeCount != currentUiState.discussion.likeCount
                if (!isToggle) return@launch
                handleResult(discussionRepository.toggleLike(discussionId ?: THROW_DISCUSSION_ID)) {
                    loadDiscussionRoom()
                }
            }
    }

    fun shareDiscussion() {
        val currentUiState = _uiState.value ?: return
        if (currentUiState !is DiscussionDetailUiState.Success) return
        onUiEvent(
            DiscussionDetailUiEvent.ShareDiscussion(
                discussionId ?: return,
                currentUiState.discussion.discussionTitle,
            ),
        )
    }

    fun navigateToProfile() {
        val currentUiState = _uiState.value ?: return
        if (currentUiState !is DiscussionDetailUiState.Success) return
        val memberId =
            currentUiState.discussion.writer.id
        _uiEvent.setValue(DiscussionDetailUiEvent.NavigateToProfile(memberId))
    }

    fun navigateToBookDiscussion() {
        val currentUiState = _uiState.value ?: return
        if (currentUiState !is DiscussionDetailUiState.Success) return
        val bookId =
            currentUiState.discussion.book.id
        _uiEvent.setValue(DiscussionDetailUiEvent.NavigateToBookDiscussions(bookId))
    }

    private suspend fun loadDiscussionRoom() {
        handleResult(
            discussionRepository.getDiscussion(
                discussionId ?: THROW_DISCUSSION_ID,
            ),
        ) { discussion ->
            _uiState.value =
                DiscussionDetailUiState.Success(
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
                when (result.exception) {
                    is TodokTodokExceptions.HttpExceptions.UnauthorizedException -> {
                        onUiEvent(DiscussionDetailUiEvent.Unauthorized(result.exception))
                    }

                    is TodokTodokExceptions.HttpExceptions.NotFoundException -> {
                        onUiEvent(DiscussionDetailUiEvent.NotFoundDiscussion(result.exception))
                    }

                    else -> {
                        onUiEvent(
                            DiscussionDetailUiEvent.ShowErrorMessage(result.exception),
                        )
                        _uiState.value = DiscussionDetailUiState.Failure(result.exception)
                    }
                }
            }
        }
    }

    private fun onUiEvent(uiEvent: DiscussionDetailUiEvent) {
        _uiEvent.setValue(uiEvent)
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussionId"
        const val KEY_MODE = "mode"

        private const val THROW_DISCUSSION_ID = -1L
    }
}
