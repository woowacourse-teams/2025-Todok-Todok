package com.team.todoktodok.presentation.view.profile.created.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.member.MemberDiscussion
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId.Companion.MemberId
import com.team.domain.repository.MemberRepository
import kotlinx.coroutines.launch

class CreatedDiscussionsViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _discussion = MutableLiveData(emptyList<MemberDiscussion>())
    val discussion: LiveData<List<MemberDiscussion>> get() = _discussion

    fun loadDiscussions(id: Long?) {
        viewModelScope.launch {
            val result =
                memberRepository.getMemberDiscussionRooms(
                    MemberId(1),
                    MemberDiscussionType.CREATED,
                )
            _discussion.value = result
        }
    }
}
