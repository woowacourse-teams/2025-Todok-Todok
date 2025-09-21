package com.team.todoktodok.presentation.compose.discussion.latest.vm

import androidx.lifecycle.viewModelScope
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.repository.DiscussionRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.team.domain.ConnectivityObserver
import com.team.domain.model.Discussion
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.compose.discussion.DiscussionsUiEvent
import com.team.todoktodok.presentation.compose.discussion.DiscussionsUiState
import com.team.todoktodok.presentation.core.base.BaseViewModel

class DiscussionsViewModel(
    private val discussionRepository: DiscussionRepository,
    private val memberRepository: MemberRepository,
    connectivityObserver: ConnectivityObserver,
) : BaseViewModel(connectivityObserver) {
    private val _uiState = MutableStateFlow(DiscussionsUiState())
    val uiState: StateFlow<DiscussionsUiState> get() = _uiState.asStateFlow()

    private val _uiEvent = Channel<DiscussionsUiEvent>(Channel.BUFFERED)
    val uiEvent get() = _uiEvent.receiveAsFlow()

    fun loadLatestDiscussions() {
        val currentState = _uiState.value
        if (!currentState.latestPageHasNext) return
        val cursor = currentState.latestPageNextCursor

        runAsync(
            key = KEY_LATEST_DISCUSSIONS,
            action = { discussionRepository.getLatestDiscussions(cursor = cursor) },
            handleSuccess = { result: LatestDiscussionPage ->
                _uiState.update { it.addLatestDiscussion(result) }
            },
            handleFailure = {
                onUiEvent(DiscussionsUiEvent.ShowErrorMessage(it))
            },
        )
    }

    fun refreshLatestDiscussions() {
        _uiState.update { it.clearLatestDiscussion() }
        loadLatestDiscussions()
    }


    fun removeDiscussion(discussionId: Long) {
        _uiState.update { it.removeDiscussion(discussionId) }
    }

    fun modifyDiscussion(discussionId: Long) {
        viewModelScope.launch {
            discussionRepository
                .getDiscussion(discussionId)
                .onSuccess { discussion: Discussion ->
                    _uiState.update { it.modifyDiscussion(discussion) }
                }.onFailure {
                    onUiEvent(DiscussionsUiEvent.ShowErrorMessage(it))
                }
        }
        onUiEvent(DiscussionsUiEvent.ClearSearchResult)
    }

    private fun onUiEvent(event: DiscussionsUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    companion object {
        private const val KEY_LATEST_DISCUSSIONS = "LATEST_DISCUSSIONS"
    }
}
