package com.team.todoktodok.presentation.view.discussiondetail.commentdetail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.ReplyRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.CommentDetailUiEvent
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.CommentDetailUiState
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.CommentDetailUiState.Companion.INIT_COMMENT
import kotlinx.coroutines.launch

class CommentDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val commentRepository: CommentRepository,
    private val replyRepository: ReplyRepository,
) : ViewModel() {
    val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

    val commentId =
        savedStateHandle.get<Long>(KEY_COMMENT_ID) ?: throw IllegalStateException()
    private val _uiState = MutableLiveData<CommentDetailUiState?>(null)
    val uiState: LiveData<CommentDetailUiState?> = _uiState

    private val _uiEvent = MutableSingleLiveData<CommentDetailUiEvent>()
    val uiEvent: SingleLiveData<CommentDetailUiEvent> = _uiEvent

    init {
        viewModelScope.launch {
            loadComment()
            loadReplies()
        }
    }

    private suspend fun loadComment() {
        val currentUiState = _uiState.value
        if (currentUiState == null) {
            _uiState.value =
                CommentDetailUiState(
                    commentRepository.getComment(discussionId, commentId),
                )
        } else {
            _uiState.value =
                currentUiState.copy(
                    comment =
                        commentRepository.getComment(
                            discussionId,
                            commentId,
                        ),
                )
        }
    }

    fun repliesReload() {
        viewModelScope.launch {
            loadReplies()
            showNewComment()
        }
    }

    fun toggleReplyLike(replyId: Long) {
        viewModelScope.launch {
            replyRepository.toggleLike(discussionId, commentId, replyId)
            loadReplies()
        }
    }

    fun toggleCommentLike(commentId: Long) {
        viewModelScope.launch {
            commentRepository.toggleLike(discussionId, commentId)
            loadComment()
        }
    }

    fun showReplyCreate() {
        _uiEvent.setValue(CommentDetailUiEvent.ShowReplyCreate(discussionId, commentId))
    }

    fun showNewComment() {
        _uiEvent.setValue(CommentDetailUiEvent.ShowNewReply)
    }

    private suspend fun loadReplies() {
        val currentUiState = _uiState.value
        if (currentUiState == null) {
            _uiState.value =
                CommentDetailUiState(
                    comment = INIT_COMMENT,
                    replies = replyRepository.getReplies(discussionId, commentId),
                )
        } else {
            _uiState.value =
                currentUiState.copy(
                    replies = replyRepository.getReplies(discussionId, commentId),
                )
        }
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussion_id"
        const val KEY_COMMENT_ID = "comment_id"
    }
}
