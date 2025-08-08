package com.team.todoktodok.presentation.view.setting.modify.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.NickNameException
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.Profile
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.setting.modify.ModifyProfileUiEvent
import kotlinx.coroutines.launch

class ModifyProfileViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    init {
        loadProfile()
    }

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> get() = _profile

    private val _uiEvent = MutableSingleLiveData<ModifyProfileUiEvent>()
    val uiEvent: SingleLiveData<ModifyProfileUiEvent> get() = _uiEvent

    private fun loadProfile() {
        viewModelScope.launch {
            val profile = memberRepository.getProfile(MemberId.Mine)
            _profile.value = profile
        }
    }

    fun modifyProfile(
        text: String,
        description: String,
    ) {
        runCatching {
            Nickname(text)
        }.onSuccess { nickname ->
            viewModelScope.launch {
                memberRepository.modifyProfile(text, description)
                _uiEvent.setValue(ModifyProfileUiEvent.OnCompleteModification)
            }
        }.onFailure { e ->
            _uiEvent.setValue(ModifyProfileUiEvent.ShowInvalidNickNameMessage(e as NickNameException))
        }
    }
}
