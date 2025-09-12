package com.team.todoktodok.presentation.view.discussiondetail.commentcreate.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.exception.NetworkResult
import com.team.domain.repository.CommentRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussiondetail.commentcreate.CommentCreateState
import com.team.todoktodok.presentation.view.discussiondetail.commentcreate.CommentCreateUiEvent
import kotlinx.coroutines.launch

class CommentCreateViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val commentRepository: CommentRepository,
) : ViewModel() {
    private val discussionId: Long =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: error("discussionId missing")

    private val commentContent: String? =
        savedStateHandle.get<String>(KEY_COMMENT_CONTENT)

    private val commentCreateState: CommentCreateState =
        CommentCreateState.create(savedStateHandle.get<Long>(KEY_COMMENT_ID))

    private val _uiEvent = MutableSingleLiveData<CommentCreateUiEvent>()
    val uiEvent: SingleLiveData<CommentCreateUiEvent> = _uiEvent

    private val _commentText = MutableLiveData("")
    val commentText: LiveData<String> = _commentText

    fun initUiState() =
        viewModelScope.launch {
            when (val state = commentCreateState) {
                CommentCreateState.Create -> initializeForCreate()
                is CommentCreateState.Update -> initializeForUpdate(state.commentId)
            }
        }

    fun onCommentChanged(text: CharSequence?) {
        _commentText.value = text?.toString().orEmpty()
    }

    fun submitComment() =
        viewModelScope.launch {
            when (val state = commentCreateState) {
                CommentCreateState.Create -> submitCreate()
                is CommentCreateState.Update -> submitUpdate(state.commentId)
            }
        }

    fun saveContent() {
        if (commentCreateState is CommentCreateState.Create) {
            onUiEvent(CommentCreateUiEvent.OnCreateDismiss(_commentText.value.orEmpty().trim()))
        }
    }

    private fun initializeForCreate() {
        onCommentChanged(commentContent)
        onUiEvent(CommentCreateUiEvent.InitState(_commentText.value.orEmpty().trim()))
    }

    private suspend fun submitCreate() {
        handleResult(
            commentRepository.saveComment(
                discussionId,
                commentText.value.orEmpty().trim(),
            ),
        ) {
            onUiEvent(CommentCreateUiEvent.SubmitComment)
        }
    }

    private suspend fun initializeForUpdate(commentId: Long) {
        when (val res = commentRepository.getComment(discussionId, commentId)) {
            is NetworkResult.Success -> {
                onCommentChanged(res.data.content)
                onUiEvent(CommentCreateUiEvent.InitState(_commentText.value.orEmpty().trim()))
            }

            is NetworkResult.Failure -> onUiEvent(CommentCreateUiEvent.ShowError(res.exception))
        }
    }

    private suspend fun submitUpdate(commentId: Long) {
        handleResult(
            commentRepository.updateComment(
                discussionId,
                commentId,
                commentText.value.orEmpty().trim(),
            ),
        ) {
            onUiEvent(CommentCreateUiEvent.SubmitComment)
        }
    }

    private fun onUiEvent(event: CommentCreateUiEvent) {
        _uiEvent.setValue(event)
    }

    private inline fun handleResult(
        result: NetworkResult<Unit>,
        onSuccess: () -> Unit,
    ) {
        when (result) {
            is NetworkResult.Success -> onSuccess()
            is NetworkResult.Failure -> onUiEvent(CommentCreateUiEvent.ShowError(result.exception))
        }
    }

    companion object {
        const val KEY_DISCUSSION_ID = "key_discussion_id"
        const val KEY_COMMENT_ID = "key_comment_id"
        const val KEY_COMMENT_CONTENT = "key_comment_content"
    }
}
