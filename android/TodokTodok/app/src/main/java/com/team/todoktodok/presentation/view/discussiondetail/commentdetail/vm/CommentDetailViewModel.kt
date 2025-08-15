package com.team.todoktodok.presentation.view.discussiondetail.commentdetail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.ReplyRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.CommentDetailUiEvent
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.CommentDetailUiState
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.CommentDetailUiState.Companion.INIT_COMMENT
import com.team.todoktodok.presentation.view.discussiondetail.model.CommentItemUiState
import com.team.todoktodok.presentation.view.discussiondetail.model.ReplyItemUiState
import kotlinx.coroutines.launch

class CommentDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val commentRepository: CommentRepository,
    private val replyRepository: ReplyRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

    val commentId =
        savedStateHandle.get<Long>(KEY_COMMENT_ID) ?: throw IllegalStateException()

    private val _uiState = MutableLiveData<CommentDetailUiState>()
    val uiState: LiveData<CommentDetailUiState> = _uiState

    private val _uiEvent = MutableSingleLiveData<CommentDetailUiEvent>()
    val uiEvent: SingleLiveData<CommentDetailUiEvent> = _uiEvent

    init {
        viewModelScope.launch {
            loadComment()
            loadReplies()
        }
    }

    fun reloadComment() {
        viewModelScope.launch {
            loadComment()
            loadReplies()
            showNewReply()
        }
    }

    fun updatedComment() {
        viewModelScope.launch {
            loadComment()
            _uiEvent.setValue(CommentDetailUiEvent.CommentUpdate)
        }
    }

    fun updateReply(
        replyId: Long,
        content: String,
    ) {
        _uiEvent.setValue(
            CommentDetailUiEvent.ShowReplyUpdate(
                discussionId,
                commentId,
                replyId,
                content,
            ),
        )
    }

    fun updateComment(content: String) {
        _uiEvent.setValue(CommentDetailUiEvent.ShowCommentUpdate(discussionId, commentId, content))
    }

    fun updateContent(content: String) {
        _uiState.value = _uiState.value?.copy(content = content)
    }

    fun deleteReply(replyId: Long) {
        viewModelScope.launch {
            replyRepository.deleteReply(discussionId, commentId, replyId)
            loadReplies()
            _uiEvent.setValue(CommentDetailUiEvent.DeleteReply)
        }
    }

    fun deleteComment() {
        viewModelScope.launch {
            commentRepository.deleteComment(discussionId, commentId)
            _uiEvent.setValue(CommentDetailUiEvent.DeleteComment)
        }
    }

    fun reportReply(replyId: Long) {
        viewModelScope.launch {
            replyRepository.report(discussionId, commentId, replyId)
        }
    }

    fun reportComment() {
        viewModelScope.launch {
            commentRepository.report(discussionId, commentId)
        }
    }

    fun toggleReplyLike(replyId: Long) {
        viewModelScope.launch {
            replyRepository.toggleLike(discussionId, commentId, replyId)
            loadReplies()
        }
    }

    fun toggleCommentLike() {
        viewModelScope.launch {
            commentRepository.toggleLike(discussionId, commentId)
            loadComment()
            _uiEvent.setValue(CommentDetailUiEvent.ToggleCommentLike)
        }
    }

    fun showReplyCreate() {
        val content = _uiState.value?.content ?: ""
        _uiEvent.setValue(CommentDetailUiEvent.ShowReplyCreate(discussionId, commentId, content))
    }

    private fun showNewReply() {
        _uiEvent.setValue(CommentDetailUiEvent.ShowNewReply)
    }

    private suspend fun loadComment() {
        val currentUiState = _uiState.value
        val comment = commentRepository.getComment(discussionId, commentId)
        val isMyComment = comment.writer.id == tokenRepository.getMemberId()
        if (currentUiState == EMPTY_UI_STATE) {
            val comment = commentRepository.getComment(discussionId, commentId)
            _uiState.value =
                CommentDetailUiState(
                    CommentItemUiState(
                        comment,
                        isMyComment,
                    ),
                )
        } else {
            _uiState.value =
                currentUiState.copy(
                    comment =
                        CommentItemUiState(
                            commentRepository.getComment(discussionId, commentId),
                            isMyComment,
                        ),
                )
        }
    }

    private suspend fun loadReplies() {
        val currentUiState = _uiState.value
        val memberId = tokenRepository.getMemberId()
        val replies =
            replyRepository.getReplies(discussionId, commentId).map { reply ->
                ReplyItemUiState(
                    reply,
                    reply.user.id == memberId,
                )
            }
        if (currentUiState == EMPTY_UI_STATE) {
            _uiState.value =
                CommentDetailUiState(
                    comment = INIT_COMMENT,
                    replies = replies,
                )
        } else {
            _uiState.value =
                currentUiState.copy(
                    replies = replies,
                )
        }
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussion_id"
        const val KEY_COMMENT_ID = "comment_id"

        private val EMPTY_UI_STATE = null
    }
}
