package com.team.todoktodok.presentation.view.setting.modify.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.NickNameException
import com.team.domain.model.member.Nickname
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.setting.modify.ModifyProfileUiEvent
import com.team.todoktodok.presentation.view.setting.modify.ProfileUiState
import kotlinx.coroutines.launch

class ModifyProfileViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(ProfileUiState())
    val uiState: LiveData<ProfileUiState> get() = _uiState

    private val _uiEvent = MutableSingleLiveData<ModifyProfileUiEvent>()
    val uiEvent: SingleLiveData<ModifyProfileUiEvent> get() = _uiEvent

    init {
        loadProfile()
    }

    private fun loadProfile() {
        changeLoadingState()
        viewModelScope.launch {
            memberRepository
                .getProfile(MemberId.Mine)
                .onSuccess {
                    _uiState.value =
                        _uiState.value?.copy(
                            profile = it,
                            isLoading = false,
                        )
                }.onFailure { onUiEvent(ModifyProfileUiEvent.ShowErrorMessage(it)) }
        }
    }

    fun checkProfileValidation(
        nickname: String,
        message: String,
    ) {
        runCatching {
            Nickname(nickname)
        }.onSuccess { nickname ->
            modifyProfile(nickname, message)
        }.onFailure { e ->
            _uiEvent.setValue(ModifyProfileUiEvent.ShowInvalidNickNameMessage(e as NickNameException))
        }
    }

    private fun modifyProfile(
        nickname: Nickname,
        message: String,
    ) {
        viewModelScope.launch {
            val currentUiState = _uiState.value ?: return@launch
            _uiState.value =
                currentUiState
                    .modifyProfile(nickname.value, message.ifBlank { EMPTY_MESSAGE })

            memberRepository
                .modifyProfile(nickname.value, message)
                .onSuccess {
                    onUiEvent(ModifyProfileUiEvent.OnCompleteModification)
                }.onFailure { onUiEvent(ModifyProfileUiEvent.ShowErrorMessage(it)) }
            changeLoadingState()
        }
    }

    fun changeLoadingState() {
        _uiState.value = _uiState.value?.toggleLoading()
    }

    private fun onUiEvent(event: ModifyProfileUiEvent) {
        _uiEvent.setValue(event)
    }

    companion object {
        private const val EMPTY_MESSAGE = ""
    }
}
