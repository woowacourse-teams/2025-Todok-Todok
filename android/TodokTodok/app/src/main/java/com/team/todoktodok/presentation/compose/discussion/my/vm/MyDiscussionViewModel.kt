package com.team.todoktodok.presentation.compose.discussion.my.vm

import androidx.lifecycle.viewModelScope
import com.team.domain.ConnectivityObserver
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionsUiEvent
import com.team.todoktodok.presentation.compose.discussion.my.MyDiscussionUiState
import com.team.todoktodok.presentation.core.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyDiscussionViewModel(
    private val memberRepository: MemberRepository,
    networkConnectivityObserver: ConnectivityObserver,
) : BaseViewModel(networkConnectivityObserver) {
    private val _uiState = MutableStateFlow(MyDiscussionUiState())
    val uiState: StateFlow<MyDiscussionUiState> get() = _uiState.asStateFlow()

    private val _uiEvent = Channel<DiscussionsUiEvent>(Channel.BUFFERED)
    val uiEvent get() = _uiEvent.receiveAsFlow()

    fun loadMyDiscussions() {
        viewModelScope.launch {
            loadCreatedDiscussions()
            loadParticipatedDiscussions()
        }
    }

    private fun loadParticipatedDiscussions() =
        runAsync(
            key = KEY_MY_PARTICIPATED_DISCUSSIONS,
            action = {
                memberRepository.getMemberDiscussionRooms(
                    MemberId.Mine,
                    MemberDiscussionType.PARTICIPATED,
                )
            },
            handleSuccess = { result -> _uiState.update { it.addParticipatedDiscussions(result) } },
            handleFailure = { onUiEvent(DiscussionsUiEvent.ShowErrorMessage(it)) },
        )

    private fun loadCreatedDiscussions() =
        runAsync(
            key = KEY_MY_CREATED_DISCUSSIONS,
            action = {
                memberRepository.getMemberDiscussionRooms(
                    MemberId.Mine,
                    MemberDiscussionType.CREATED,
                )
            },
            handleSuccess = { result -> _uiState.update { it.addCreatedDiscussions(result) } },
            handleFailure = { onUiEvent(DiscussionsUiEvent.ShowErrorMessage(it)) },
        )

    private fun onUiEvent(event: DiscussionsUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    companion object {
        private const val KEY_MY_CREATED_DISCUSSIONS = "MY_CREATED_DISCUSSIONS"
        private const val KEY_MY_PARTICIPATED_DISCUSSIONS = "MY_PARTICIPATED_DISCUSSIONS"
    }
}
