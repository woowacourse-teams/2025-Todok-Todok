package com.team.todoktodok.presentation.view.profile.joined.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.member.MemberDiscussion
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId.Companion.MemberId
import com.team.domain.repository.MemberRepository
import kotlinx.coroutines.launch

class JoinedDiscussionsViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _discussion = MutableLiveData(emptyList<MemberDiscussion>())
    val discussion: LiveData<List<MemberDiscussion>> get() = _discussion

    fun loadDiscussions(id: Long) {
        viewModelScope.launch {
            val result =
                memberRepository.getMemberDiscussionRooms(
                    MemberId(id),
                    MemberDiscussionType.PARTICIPATED,
                )
            _discussion.value = result
        }
    }
}
