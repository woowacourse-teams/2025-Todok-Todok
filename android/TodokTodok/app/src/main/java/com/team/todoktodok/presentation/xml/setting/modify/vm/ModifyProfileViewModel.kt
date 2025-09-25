package com.team.todoktodok.presentation.xml.setting.modify.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.NickNameException
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.ProfileException
import com.team.domain.model.member.ProfileMessage
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.xml.setting.modify.ModifyProfileUiEvent
import com.team.todoktodok.presentation.xml.setting.modify.ProfileUiState
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

    private fun loadProfile() =
        withLoading {
            memberRepository
                .getProfile(MemberId.Mine)
                .onSuccess {
                    _uiState.value =
                        _uiState.value?.modifyProfile(
                            it.nickname,
                            it.message ?: EMPTY_MESSAGE,
                        )
                }.onFailure { onUiEvent(ModifyProfileUiEvent.ShowErrorMessage(it)) }
        }

    fun checkProfileValidation(
        nickname: String,
        message: String,
    ) {
        runCatching {
            val currentProfile = _uiState.value?.profile ?: return
            val newNickname = Nickname(nickname)
            val newMessage = ProfileMessage(message)

            if (currentProfile.nickname == newNickname.value) {
                throw NickNameException.SameNicknameModification
            }
            if (message.isNotEmpty() && currentProfile.message == newMessage.value) {
                throw ProfileException.SameMessageModification
            }
            newNickname to newMessage
        }.onSuccess { (nickname, message) ->
            modifyProfile(nickname, message)
        }.onFailure { e ->
            when (e) {
                is ProfileException -> onUiEvent(ModifyProfileUiEvent.ShowInvalidMessageMessage(e))
                is NickNameException -> onUiEvent(ModifyProfileUiEvent.ShowInvalidNickNameMessage(e))
                else -> onUiEvent(ModifyProfileUiEvent.ShowErrorMessage((e as TodokTodokExceptions)))
            }
        }
    }

    private fun modifyProfile(
        nickname: Nickname,
        message: ProfileMessage,
    ) = withLoading {
        val currentUiState = _uiState.value ?: return@withLoading

        _uiState.value =
            currentUiState
                .modifyProfile(
                    nickname.value,
                    message.value.ifBlank { EMPTY_MESSAGE },
                )
        memberRepository
            .modifyProfile(nickname, message)
            .onSuccess {
                onUiEvent(ModifyProfileUiEvent.OnCompleteModification)
            }.onFailure { onUiEvent(ModifyProfileUiEvent.ShowErrorMessage(it)) }
    }

    private fun onUiEvent(event: ModifyProfileUiEvent) {
        _uiEvent.setValue(event)
    }

    private fun withLoading(action: suspend () -> Unit) {
        viewModelScope.launch {
            changeLoadingState()
            action()
            changeLoadingState()
        }
    }

    private fun changeLoadingState() {
        _uiState.value = _uiState.value?.toggleLoading()
    }

    companion object {
        private const val EMPTY_MESSAGE = ""
    }
}
