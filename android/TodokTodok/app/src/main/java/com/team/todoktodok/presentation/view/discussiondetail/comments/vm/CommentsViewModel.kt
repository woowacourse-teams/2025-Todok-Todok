package com.team.todoktodok.presentation.view.discussiondetail.comments.vm

import android.os.Parcelable
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
import com.team.todoktodok.presentation.view.discussiondetail.comments.CommentsUiState
import com.team.todoktodok.presentation.view.discussiondetail.model.CommentItemUiState
import kotlinx.coroutines.launch

class CommentsViewModel(
    savedStateHandle: SavedStateHandle,
    private val commentRepository: CommentRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

    private val _uiState = MutableLiveData(CommentsUiState())
    val uiState: LiveData<CommentsUiState> = _uiState

    private val _uiEvent = MutableSingleLiveData<CommentsUiEvent>()
    val uiEvent: SingleLiveData<CommentsUiEvent> = _uiEvent

    var commentsRvState: Parcelable? = null

    init {
        viewModelScope.launch {
            loadComments()
        }
    }

    fun loadCommentsShowState(showState: Parcelable?) {
        commentsRvState = showState
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

    fun updateComment(
        commentId: Long,
        content: String,
    ) {
        _uiEvent.setValue(CommentsUiEvent.ShowCommentUpdate(discussionId, commentId, content))
    }

    fun reportComment(commentId: Long) {
        viewModelScope.launch {
            commentRepository.report(discussionId, commentId)
        }
    }

    fun showCommentCreate() {
        _uiEvent.setValue(
            CommentsUiEvent.ShowCommentCreate(
                discussionId,
                _uiState.value?.commentContent ?: "",
            ),
        )
    }

    fun updateCommentContent(content: String) {
        _uiState.value = _uiState.value?.copy(commentContent = content)
    }

    private fun showNewComment() {
        _uiEvent.setValue(CommentsUiEvent.ShowNewComment)
    }

    private suspend fun loadComments() {
        _uiState.value =
            _uiState.value?.copy(
                commentRepository
                    .getCommentsByDiscussionRoomId(discussionId)
                    .map {
                        CommentItemUiState(
                            it,
                            isMyComment = tokenRepository.getMemberId() == it.writer.id,
                        )
                    },
            )
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussionId"
    }
}
