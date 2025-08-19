package com.team.todoktodok.presentation.view.setting.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.setting.SettingScreen
import com.team.todoktodok.presentation.view.setting.SettingUiState
import com.team.todoktodok.presentation.view.setting.modify.ModifyProfileUiEvent

class SettingViewModel : ViewModel() {
    private val _uiState = MutableLiveData(SettingUiState())
    val uiState: LiveData<SettingUiState> get() = _uiState

    private val _uiEvent = MutableSingleLiveData<ModifyProfileUiEvent>()
    val uiEvent: SingleLiveData<ModifyProfileUiEvent> get() = _uiEvent

    fun changeScreen(screen: SettingScreen) {
        _uiState.value = _uiState.value?.copy(screen)
    }
}
