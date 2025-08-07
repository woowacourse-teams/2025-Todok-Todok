package com.team.todoktodok.presentation.view.discussiondetail.comments.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Comment
import com.team.domain.repository.CommentRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussiondetail.comments.CommentsUiEvent
import kotlinx.coroutines.launch

class CommentsViewModel(
    savedStateHandle: SavedStateHandle,
    private val commentRepository: CommentRepository,
) : ViewModel() {
    val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> = _comments

    private val _uiEvent = MutableSingleLiveData<CommentsUiEvent>()
    val uiEvent: SingleLiveData<CommentsUiEvent> = _uiEvent

    init {
        viewModelScope.launch {
            loadComments()
        }
    }

    fun commentsReload() {
        viewModelScope.launch {
            loadComments()
            showNewComment()
        }
    }

    fun deleteComment(commentId: Long) {
        viewModelScope.launch {
            commentRepository.deleteComment(discussionId, commentId)
            loadComments()
        }
    }

    fun updateComment(commentId: Long) {
        viewModelScope.launch {
            _uiEvent.setValue(CommentsUiEvent.ShowCommentUpdate(discussionId, commentId))
        }
    }

    fun showCommentCreate() {
        _uiEvent.setValue(CommentsUiEvent.ShowCommentCreate(discussionId))
    }

    private fun showNewComment() {
        _uiEvent.setValue(CommentsUiEvent.ShowNewComment)
    }

    private suspend fun loadComments() {
        _comments.value =
            commentRepository
                .getCommentsByDiscussionRoomId(discussionId)
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussionId"
    }
}
