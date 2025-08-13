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
    private val _uiState = MutableLiveData<ProfileUiState>()
    val uiState: LiveData<ProfileUiState> get() = _uiState

    private val _uiEvent = MutableSingleLiveData<ProfileUiEvent>()
    val uiEvent: SingleLiveData<ProfileUiEvent> get() = _uiEvent

    fun initState(id: Long) {
        val memberId = MemberId(id)
        _uiState.value = ProfileUiState.initial(memberId)
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value?.memberId?.let {
                val result = memberRepository.getProfile(it)
                _uiState.value = _uiState.value?.modifyProfile(result)
            }
        }
    }

    fun supportMember(type: Support) {
        viewModelScope.launch {
            val memberId = _uiState.value?.memberId
            if (memberId is MemberId.OtherUser) {
                memberRepository.supportMember(memberId, type)
                onUiEvent(ProfileUiEvent.OnCompleteSupport(type))
            }
        }
    }

    private fun onUiEvent(event: ProfileUiEvent) {
        _uiEvent.setValue(event)
    }
}
