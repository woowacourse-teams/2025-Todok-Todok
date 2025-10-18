package com.team.todoktodok.presentation.compose.discussion.latest.vm

import androidx.lifecycle.viewModelScope
import com.team.domain.ConnectivityObserver
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsUiEvent
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsUiState
import com.team.todoktodok.presentation.core.base.BaseViewModel
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LatestDiscussionViewModel(
    private val discussionRepository: DiscussionRepository,
    networkConnectivityObserver: ConnectivityObserver,
) : BaseViewModel(networkConnectivityObserver) {
    private val _uiState = MutableStateFlow(LatestDiscussionsUiState())
    val uiState: StateFlow<LatestDiscussionsUiState> get() = _uiState.asStateFlow()

    private val _uiEvent = Channel<LatestDiscussionsUiEvent>(Channel.BUFFERED)
    val uiEvent get() = _uiEvent.receiveAsFlow()

    fun loadLatestDiscussions() {
        val currentState = _uiState.value
        if (!currentState.hasNext || (isLoading.value)) return
        val cursor = currentState.nextCursor

        runAsync(
            key = KEY_LATEST_DISCUSSIONS,
            action = { discussionRepository.getLatestDiscussions(cursor = cursor) },
            handleSuccess = { result ->
                _uiState.update { it.append(result) }
            },
            handleFailure = { onUiEvent(LatestDiscussionsUiEvent.ShowErrorMessage(it)) },
        )
    }

    fun refreshLatestDiscussions() {
        _uiState.update { it.clearForRefresh() }
        loadLatestDiscussions()
    }

    fun removeDiscussion(discussionId: Long) {
        _uiState.update { it.remove(discussionId) }
    }

    fun modifyDiscussion(discussion: SerializationDiscussion) {
        _uiState.update { it.modify(discussion) }
    }

    private fun onUiEvent(event: LatestDiscussionsUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    companion object {
        private const val KEY_LATEST_DISCUSSIONS = "LATEST_DISCUSSIONS"
    }
}
