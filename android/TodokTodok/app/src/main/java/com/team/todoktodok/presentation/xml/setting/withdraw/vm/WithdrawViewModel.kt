package com.team.todoktodok.presentation.xml.setting.withdraw.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.NotificationRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
<<<<<<< HEAD:android/TodokTodok/app/src/main/java/com/team/todoktodok/presentation/xml/setting/withdraw/vm/WithdrawViewModel.kt
import com.team.todoktodok.presentation.xml.setting.withdraw.WithdrawUiEvent
=======
import com.team.todoktodok.presentation.view.setting.withdraw.WithdrawUiEvent
import kotlinx.coroutines.joinAll
>>>>>>> 673c9957 (feat: #638 회원 탈퇴 시 푸시 알림 정보 삭제 기능 추가):android/TodokTodok/app/src/main/java/com/team/todoktodok/presentation/view/setting/withdraw/vm/WithdrawViewModel.kt
import kotlinx.coroutines.launch

class WithdrawViewModel(
    private val memberRepository: MemberRepository,
    private val notificationRepository: NotificationRepository,
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
                launch { notificationRepository.deletePushNotification() },
            )
            _isLoading.value = false
            _uiEvent.setValue(WithdrawUiEvent.NavigateToLogin)
        }
    }
}
