package com.team.todoktodok.presentation.view.profile.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.member.MemberId.Companion.MemberId
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.view.profile.ProfileUiState
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(ProfileUiState.initial())
    val uiState: LiveData<ProfileUiState> get() = _uiState

    fun loadProfile(memberId: Long?) {
        viewModelScope.launch {
            val result = memberRepository.getProfile(MemberId(1))
            _uiState.value = _uiState.value?.modifyProfile(result)
        }
    }
}
