package com.team.todoktodok.presentation.view.discussiondetail.replycreate.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.repository.CommentRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussiondetail.replycreate.ReplyCreateUiEvent
import kotlinx.coroutines.launch

class ReplyCreateViewModel(
    savedStateHandle: SavedStateHandle,
    private val commentRepository: CommentRepository,
) : ViewModel() {
    private val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

    private val commentId =
        savedStateHandle.get<Long>(KEY_COMMENT_ID) ?: throw IllegalStateException()

    private val _uiEvent = MutableSingleLiveData<ReplyCreateUiEvent>()
    val uiEvent: SingleLiveData<ReplyCreateUiEvent> = _uiEvent

    private val _commentText = MutableLiveData("")
    val commentText: LiveData<String> = _commentText

    fun onCommentChanged(text: CharSequence?) {
        _commentText.value = text?.toString() ?: ""
    }

    fun submitReply() {
        viewModelScope.launch {
            _uiEvent.setValue(ReplyCreateUiEvent.CreateReply)
        }
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussionId"
        const val KEY_COMMENT_ID = "comment_id"
    }
}
