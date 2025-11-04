package com.team.todoktodok.presentation.xml.setting.modify.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.core.event.MutableSingleLiveData
import com.team.core.event.SingleLiveData
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.model.exception.toDomain
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.NickNameException
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.ProfileException
import com.team.domain.model.member.ProfileMessage
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.xml.setting.modify.ModifyProfileUiEvent
import com.team.todoktodok.presentation.xml.setting.modify.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModifyProfileViewModel
    @Inject
    constructor(
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
            val currentState = _uiState.value ?: return
            val newUiState = currentState.modifyProfile(nickname, message)

            when {
                !newUiState.isNicknameChange && !newUiState.isProfileMessageChange -> {
                    onUiEvent(ModifyProfileUiEvent.ShowInvalidMessageMessage(ProfileException.SameMessageModification))
                    onUiEvent(ModifyProfileUiEvent.ShowInvalidNickNameMessage(NickNameException.SameNicknameModification))
                }

                else -> {
                    runCatching {
                        Nickname(nickname) to ProfileMessage(message)
                    }.onSuccess {
                        val (newNickname, newMessage) = it
                        modifyProfile(newNickname, newMessage)
                    }.onFailure { exception ->
                        when (exception) {
                            is NickNameException ->
                                onUiEvent(
                                    ModifyProfileUiEvent.ShowInvalidNickNameMessage(
                                        exception,
                                    ),
                                )

                            else -> onUiEvent(ModifyProfileUiEvent.ShowErrorMessage(exception.toDomain()))
                        }
                    }
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
