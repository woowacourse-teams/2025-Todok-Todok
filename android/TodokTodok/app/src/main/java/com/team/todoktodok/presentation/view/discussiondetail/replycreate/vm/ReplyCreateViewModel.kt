package com.team.todoktodok.presentation.view.discussiondetail.replycreate.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.repository.ReplyRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussiondetail.replycreate.ReplyCreateUiEvent
import kotlinx.coroutines.launch

class ReplyCreateViewModel(
    savedStateHandle: SavedStateHandle,
    private val replyRepository: ReplyRepository,
) : ViewModel() {
    private val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

    private val commentId =
        savedStateHandle.get<Long>(KEY_COMMENT_ID) ?: throw IllegalStateException()

    private val replyId = savedStateHandle.get<Long>(KEY_REPLY_ID)

    private val content = savedStateHandle.get<String>(KEY_REPLY_CONTENT)

    private val _uiEvent = MutableSingleLiveData<ReplyCreateUiEvent>()
    val uiEvent: SingleLiveData<ReplyCreateUiEvent> = _uiEvent

    private val _commentText = MutableLiveData("")
    val commentText: LiveData<String> = _commentText

    init {
        viewModelScope.launch {
            if (replyId != null) {
                onCommentChanged(content ?: "")
            }
        }
    }

    fun onCommentChanged(text: CharSequence?) {
        _commentText.value = text?.toString() ?: ""
    }

    fun submitReply() {
        viewModelScope.launch {
            if (replyId == null) {
                replyRepository.saveReply(
                    discussionId,
                    commentId,
                    commentText.value ?: throw IllegalStateException(),
                )
                _uiEvent.setValue(ReplyCreateUiEvent.CreateReply)
            } else {
                replyRepository.updateReply(
                    discussionId,
                    commentId,
                    replyId,
                    commentText.value ?: throw IllegalStateException(),
                )
                _uiEvent.setValue(ReplyCreateUiEvent.CreateReply)
            }
        }
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussionId"
        const val KEY_COMMENT_ID = "comment_id"
        const val KEY_REPLY_ID = "reply_id"
        const val KEY_REPLY_CONTENT = "reply_content"
    }
}
