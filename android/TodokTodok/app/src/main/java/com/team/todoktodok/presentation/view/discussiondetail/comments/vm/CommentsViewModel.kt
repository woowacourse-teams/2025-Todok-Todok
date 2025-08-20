package com.team.todoktodok.presentation.view.discussiondetail.comments.vm

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.exception.NetworkResult
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
    val discussionId: Long =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: error("discussionId missing")

    private val _uiState = MutableLiveData(CommentsUiState())
    val uiState: LiveData<CommentsUiState> = _uiState

    private val _uiEvent = MutableSingleLiveData<CommentsUiEvent>()
    val uiEvent: SingleLiveData<CommentsUiEvent> = _uiEvent

    var commentsRvState: Parcelable? = null

    init {
        viewModelScope.launch { loadComments() }
    }

    fun loadCommentsShowState(showState: Parcelable?) {
        commentsRvState = showState
    }

    fun reloadComments() =
        viewModelScope.launch {
            loadComments()
            onUiEvent(CommentsUiEvent.ShowNewComment)
        }

    fun toggleLike(commentId: Long) =
        viewModelScope.launch {
            handleResult(commentRepository.toggleLike(discussionId, commentId)) {
                loadComments()
            }
        }

    fun deleteComment(commentId: Long) =
        viewModelScope.launch {
            handleResult(commentRepository.deleteComment(discussionId, commentId)) {
                loadComments()
                onUiEvent(CommentsUiEvent.DeleteComment)
            }
        }

    fun updateComment(
        commentId: Long,
        content: String,
    ) {
        onUiEvent(CommentsUiEvent.ShowCommentUpdate(discussionId, commentId, content))
    }

    fun reportComment(commentId: Long) =
        viewModelScope.launch {
            handleResult(
                commentRepository.report(discussionId, commentId),
            ) {}
        }

    fun showCommentCreate() {
        onUiEvent(
            CommentsUiEvent.ShowCommentCreate(
                discussionId,
                _uiState.value?.commentContent.orEmpty(),
            ),
        )
    }

    fun updateCommentContent(content: String) {
        _uiState.value = _uiState.value?.copy(commentContent = content)
    }

    private suspend fun loadComments() {
        val result = commentRepository.getCommentsByDiscussionId(discussionId)
        handleResult(result) { list ->
            val myId = tokenRepository.getMemberId()
            val items = list.map { CommentItemUiState(it, isMyComment = myId == it.writer.id) }
            _uiState.value = _uiState.value?.copy(comments = items)
        }
    }

    private fun onUiEvent(event: CommentsUiEvent) {
        _uiEvent.setValue(event)
    }

    private inline fun <T> handleResult(
        result: NetworkResult<T>,
        onSuccess: (T) -> Unit,
    ) {
        when (result) {
            is NetworkResult.Success -> onSuccess(result.data)
            is NetworkResult.Failure -> onUiEvent(CommentsUiEvent.ShowError(result.exception))
        }
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussionId"
    }
}
