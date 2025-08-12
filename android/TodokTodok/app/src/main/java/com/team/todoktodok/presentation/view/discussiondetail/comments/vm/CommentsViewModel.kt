package com.team.todoktodok.presentation.view.discussiondetail.comments.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussiondetail.comments.CommentsUiEvent
import com.team.todoktodok.presentation.view.discussiondetail.model.CommentUiModel
import kotlinx.coroutines.launch

class CommentsViewModel(
    savedStateHandle: SavedStateHandle,
    private val commentRepository: CommentRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

    private val _comments = MutableLiveData<List<CommentUiModel>>()
    val comments: LiveData<List<CommentUiModel>> = _comments

    private val _uiEvent = MutableSingleLiveData<CommentsUiEvent>()
    val uiEvent: SingleLiveData<CommentsUiEvent> = _uiEvent

    init {
        viewModelScope.launch {
            loadComments()
        }
    }

    fun reloadComments() {
        viewModelScope.launch {
            loadComments()
            showNewComment()
        }
    }

    fun toggleLike(commentId: Long) {
        viewModelScope.launch {
            commentRepository.toggleLike(discussionId, commentId)
            loadComments()
        }
    }

    fun deleteComment(commentId: Long) {
        viewModelScope.launch {
            commentRepository.deleteComment(discussionId, commentId)
            loadComments()
            _uiEvent.setValue(CommentsUiEvent.DeleteComment)
        }
    }

    fun updateComment(commentId: Long) {
        viewModelScope.launch {
            _uiEvent.setValue(CommentsUiEvent.ShowCommentUpdate(discussionId, commentId))
        }
    }

    fun report(commentId: Long) {
        viewModelScope.launch {
            commentRepository.report(discussionId, commentId)
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
                .map {
                    CommentUiModel(
                        it,
                        isMyComment = tokenRepository.getMemberId() == it.writer.id,
                    )
                }
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussionId"
    }
}
