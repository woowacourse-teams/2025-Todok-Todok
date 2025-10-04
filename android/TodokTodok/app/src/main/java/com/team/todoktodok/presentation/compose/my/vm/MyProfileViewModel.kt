package com.team.todoktodok.presentation.compose.my.vm

import androidx.lifecycle.viewModelScope
import com.team.domain.ConnectivityObserver
import com.team.domain.model.member.MemberId
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.compose.my.model.MyProfileUiEvent
import com.team.todoktodok.presentation.compose.my.model.MyProfileUiState
import com.team.todoktodok.presentation.core.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MyProfileViewModel(
    private val memberRepository: MemberRepository,
    connectivityObserver: ConnectivityObserver,
) : BaseViewModel(connectivityObserver) {
    private val _uiState = MutableStateFlow(MyProfileUiState())
    val uiState: StateFlow<MyProfileUiState> get() = _uiState.asStateFlow()

    private val _uiEvent = Channel<MyProfileUiEvent>(Channel.BUFFERED)
    val uiEvent get() = _uiEvent.receiveAsFlow()

    fun loadProfile() =
        runAsync(
            key = KEY_FETCH_PROFILE,
            action = { memberRepository.getProfile(MemberId.Mine) },
            handleSuccess = { _uiState.value = _uiState.value.copy(profile = it) },
            handleFailure = { onUiEvent(MyProfileUiEvent.ShowErrorMessage(it)) },
        )

    private fun onUiEvent(event: MyProfileUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    companion object {
        private const val KEY_FETCH_PROFILE = "fetch_profile"
    }
}
