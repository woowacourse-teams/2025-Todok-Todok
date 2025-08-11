package com.team.todoktodok.presentation.view.profile.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Support
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.MemberId.Companion.MemberId
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.profile.ProfileUiEvent
import com.team.todoktodok.presentation.view.profile.ProfileUiState
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(ProfileUiState.initial())
    val uiState: LiveData<ProfileUiState> get() = _uiState

    private val _uiEvent = MutableSingleLiveData<ProfileUiEvent>()
    val uiEvent: SingleLiveData<ProfileUiEvent> get() = _uiEvent

    fun loadProfile(memberId: Long) {
        viewModelScope.launch {
            val memberId = MemberId(memberId)
            val result = memberRepository.getProfile(memberId)
            _uiState.value = _uiState.value?.modifyProfile(result, memberId)
        }
    }

    fun supportMember(type: Support) {
        viewModelScope.launch {
            val memberId = _uiState.value?.memberId
            if (memberId is MemberId.OtherUser) {
                memberRepository.supportMember(MemberId.OtherUser(2), type)
                onUiEvent(ProfileUiEvent.OnCompleteSupport(type))
            }
        }
    }

    private fun onUiEvent(event: ProfileUiEvent) {
        _uiEvent.setValue(event)
    }
}
