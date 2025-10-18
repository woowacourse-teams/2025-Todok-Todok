package com.team.todoktodok.presentation.xml.draft.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.discussionroom.DiscussionRoom
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.xml.draft.DraftUiEvent
import kotlinx.coroutines.launch

class DraftsViewModel(
    private val discussionRepository: DiscussionRepository,
) : ViewModel() {
    private val _drafts: MutableLiveData<List<DiscussionRoom>> = MutableLiveData(emptyList())
    val drafts: LiveData<List<DiscussionRoom>> get() = _drafts
    private val _uiEvent: MutableSingleLiveData<DraftUiEvent> = MutableSingleLiveData()
    val uiEvent: SingleLiveData<DraftUiEvent> get() = _uiEvent

    init {
        initPage()
    }

    fun initPage() {
        viewModelScope.launch {
            val result = discussionRepository.getDiscussions()
            _drafts.value = result
        }
    }

    fun selectDraft(position: Int) {
        val id = _drafts.value?.get(position)?.id ?: error("잠시 오류가 생겼습니다. 다시 선택해주세요")
        _uiEvent.setValue(DraftUiEvent.NavigateToCreateDiscussionRoom(id))
    }
}
