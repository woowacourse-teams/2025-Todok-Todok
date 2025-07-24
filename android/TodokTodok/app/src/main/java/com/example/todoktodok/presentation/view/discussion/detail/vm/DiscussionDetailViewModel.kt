package com.example.todoktodok.presentation.view.discussion.detail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Comment
import com.example.domain.model.Discussion
import com.example.domain.repository.CommentRepository
import com.example.domain.repository.DiscussionRepository
import com.example.todoktodok.presentation.core.event.MutableSingleLiveData
import com.example.todoktodok.presentation.core.event.SingleLiveData
import com.example.todoktodok.presentation.view.discussion.detail.DiscussionDetailUiEvent
import kotlinx.coroutines.launch

class DiscussionDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val discussionRepository: DiscussionRepository,
    private val commentRepository: CommentRepository,
) : ViewModel() {
    private val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()
    private val _discussion = MutableLiveData<Discussion>()
    val discussion: LiveData<Discussion> = _discussion

    private val _comments = MutableLiveData<List<Comment>>(emptyList())
    val comments: LiveData<List<Comment>> = _comments

    private val _commentText = MutableLiveData("")
    val commentText: LiveData<String> = _commentText

    private val _uiEvent = MutableSingleLiveData<DiscussionDetailUiEvent>()
    val uiEvent: SingleLiveData<DiscussionDetailUiEvent> = _uiEvent

    init {
        viewModelScope.launch {
            loadDiscussionRoom()
        }
        loadComments()
    }

    fun onUiEvent(uiEvent: DiscussionDetailUiEvent) {
        _uiEvent.setValue(uiEvent)
    }

    fun submitComment() {
        viewModelScope.launch {
            commentRepository.saveComment(discussionId, commentText.value ?: "")
            loadComments()
        }
    }

    fun onCommentChanged(text: CharSequence?) {
        _commentText.value = text?.toString() ?: ""
    }

    private suspend fun loadDiscussionRoom() {
        _discussion.value =
            discussionRepository.getDiscussion(discussionId).getOrNull()
    }

    fun loadComments() {
        viewModelScope.launch {
            _comments.value = commentRepository.getCommentsByDiscussionRoomId(discussionId)
        }
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussionId"
    }
}
