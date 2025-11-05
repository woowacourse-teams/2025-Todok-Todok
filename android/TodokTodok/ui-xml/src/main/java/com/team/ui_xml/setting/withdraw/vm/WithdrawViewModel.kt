package com.team.ui_xml.setting.withdraw.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.core.event.MutableSingleLiveData
import com.team.core.event.SingleLiveData
import com.team.domain.repository.MemberRepository
import com.team.ui_xml.setting.withdraw.WithdrawUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithdrawViewModel
    @Inject
    constructor(
        private val memberRepository: MemberRepository,
    ) : ViewModel() {
        private val _isLoading = MutableLiveData<Boolean>()
        val isLoading: LiveData<Boolean> get() = _isLoading

        private val _uiEvent = MutableSingleLiveData<WithdrawUiEvent>()
        val uiEvent: SingleLiveData<WithdrawUiEvent> get() = _uiEvent

        fun withdraw() {
            _isLoading.value = true
            viewModelScope.launch {
                joinAll(
                    launch { memberRepository.withdraw() },
                )
                _isLoading.value = false
                _uiEvent.setValue(WithdrawUiEvent.NavigateToLogin)
            }
        }
    }
