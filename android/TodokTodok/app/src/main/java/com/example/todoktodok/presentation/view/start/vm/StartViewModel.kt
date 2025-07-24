package com.example.todoktodok.presentation.view.start.vm

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Member
import com.example.domain.repository.MemberRepository
import kotlinx.coroutines.launch

class StartViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _member = MutableLiveData<Member>()
    val member: LiveData<Member> get() = _member

    fun login(
        email: String,
        nickname: String?,
        profileImage: Uri?,
    ) {
        setUpMember(email, nickname, profileImage)

        viewModelScope.launch {
            memberRepository.login(email)
        }
    }

    private fun setUpMember(
        email: String,
        nickname: String?,
        profileImage: Uri?,
    ) {
        _member.value =
            Member(
                nickname ?: NOT_EXIST_NICKNAME,
                profileImage?.toString() ?: NOT_EXIST_PROFILE_IMAGE,
                email,
            )
    }

    fun signUp(
        email: String,
        nickname: String,
        profileImage: String,
    ) {
        viewModelScope.launch {
            memberRepository.signUp(
                Member(
                    email,
                    nickname,
                    profileImage,
                ),
            )
        }
    }

    companion object {
        private const val NOT_EXIST_NICKNAME = ""
        private const val NOT_EXIST_PROFILE_IMAGE = ""
    }
}
