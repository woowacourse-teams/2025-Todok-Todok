package com.team.todoktodok.presentation.compose.discussion.my.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.ConnectivityObserver
import com.team.domain.repository.MemberRepository

class MyDiscussionViewModelFactory(
    private val memberRepository: MemberRepository,
    private val networkConnectivityObserver: ConnectivityObserver,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyDiscussionViewModel::class.java)) {
            return MyDiscussionViewModel(memberRepository, networkConnectivityObserver) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
