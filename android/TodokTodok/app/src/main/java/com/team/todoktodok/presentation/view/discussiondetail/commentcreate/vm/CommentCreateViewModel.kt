package com.team.todoktodok.presentation.view.discussiondetail.commentcreate.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.repository.CommentRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussiondetail.CommentCreateUiEvent
import kotlinx.coroutines.launch

class CommentCreateViewModel(
    savedStateHandle: SavedStateHandle,
    private val commentRepository: CommentRepository,
) : ViewModel() {
    private val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

    private val _uiEvent = MutableSingleLiveData<CommentCreateUiEvent>()
    val uiEvent: SingleLiveData<CommentCreateUiEvent> = _uiEvent

    private val _commentText = MutableLiveData("")
    val commentText: LiveData<String> = _commentText

    fun onCommentChanged(text: CharSequence?) {
        _commentText.value = text?.toString() ?: ""
    }

    fun submitComment() {
        viewModelScope.launch {
            commentRepository.saveComment(discussionId, commentText.value ?: "")
            _uiEvent.setValue(CommentCreateUiEvent.CreateComment)
        }
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussionId"
    }
}
