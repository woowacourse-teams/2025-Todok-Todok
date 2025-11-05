package com.team.ui_xml.setting.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.core.event.MutableSingleLiveData
import com.team.core.event.SingleLiveData
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.view.setting.SettingUiEvent
import com.team.ui_xml.setting.SettingScreen
import com.team.ui_xml.setting.SettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
    @Inject
    constructor(
        private val tokenRepository: TokenRepository,
    ) : ViewModel() {
        private val _uiState = MutableLiveData(SettingUiState())
        val uiState: LiveData<SettingUiState> get() = _uiState

        private val _uiEvent = MutableSingleLiveData<SettingUiEvent>()
        val uiEvent: SingleLiveData<SettingUiEvent> get() = _uiEvent

        fun changeScreen(screen: SettingScreen) {
            _uiState.value = _uiState.value?.copy(screen)
        }

        fun logout() {
            viewModelScope.launch {
                tokenRepository.logout()
                _uiEvent.setValue(SettingUiEvent.NavigateToLogin)
            }
        }
    }
