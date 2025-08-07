package com.team.todoktodok.presentation.view.profile.joined.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.member.MemberDiscussion
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId.Companion.MemberId
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.profile.created.MemberDiscussionUiEvent
import kotlinx.coroutines.launch

class JoinedDiscussionsViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _discussion = MutableLiveData(emptyList<MemberDiscussion>())
    val discussion: LiveData<List<MemberDiscussion>> get() = _discussion

    private val _uiEvent = MutableSingleLiveData<MemberDiscussionUiEvent>()
    val uiEvent: SingleLiveData<MemberDiscussionUiEvent> get() = _uiEvent

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

    fun findSelectedDiscussion(index: Int) {
        val selectedDiscussion = _discussion.value?.get(index)
        requireNotNull(selectedDiscussion) { INVALID_DISCUSSION_INDEX.format(index) }

        onUiEvent(MemberDiscussionUiEvent.NavigateToDetail(selectedDiscussion.id))
    }

    private fun onUiEvent(event: MemberDiscussionUiEvent) {
        _uiEvent.setValue(event)
    }

    companion object {
        private const val INVALID_DISCUSSION_INDEX = "토론 목록에서 인덱스 %d는 유효하지 않습니다."
    }
}
