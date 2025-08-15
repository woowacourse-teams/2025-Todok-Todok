package com.team.todoktodok.presentation.view.discussiondetail.commentcreate.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

    val commentContent = savedStateHandle.get<String>(KEY_COMMENT_CONTENT)

    private val commentCreateState =
        CommentCreateState.create(savedStateHandle.get<Long>(KEY_COMMENT_ID))

    private val _uiEvent = MutableSingleLiveData<CommentCreateUiEvent>()
    val uiEvent: SingleLiveData<CommentCreateUiEvent> = _uiEvent

    private val _commentText = MutableLiveData("")
    val commentText: LiveData<String> = _commentText

    fun initUiState() {
        viewModelScope.launch {
            when (commentCreateState) {
                CommentCreateState.Create -> {
                    onCommentChanged(commentContent)
                }

                is CommentCreateState.Update -> {
                    val content =
                        commentRepository
                            .getComment(
                                discussionId,
                                commentCreateState.commentId,
                            ).content
                    onCommentChanged(content)
                }
            }
            _uiEvent.setValue(CommentCreateUiEvent.InitState(_commentText.value ?: ""))
        }
    }

    fun onCommentChanged(text: CharSequence?) {
        _commentText.value = text?.toString() ?: ""
    }

    fun submitComment() {
        viewModelScope.launch {
            when (commentCreateState) {
                CommentCreateState.Create -> {
                    commentRepository.saveComment(discussionId, commentText.value ?: "")
                    _uiEvent.setValue(CommentCreateUiEvent.SubmitComment)
                }

                is CommentCreateState.Update -> {
                    commentRepository.updateComment(
                        discussionId,
                        commentCreateState.commentId,
                        commentText.value ?: "",
                    )
                    _uiEvent.setValue(CommentCreateUiEvent.SubmitComment)
                }
            }
        }
    }

    fun saveContent() {
        when (commentCreateState) {
            CommentCreateState.Create -> {
                _uiEvent.setValue(
                    CommentCreateUiEvent.OnCreateDismiss(
                        _commentText.value ?: "",
                    ),
                )
            }

            is CommentCreateState.Update -> Unit
        }
    }

    companion object {
        const val KEY_DISCUSSION_ID = "key_discussion_id"
        const val KEY_COMMENT_ID = "key_comment_id"

        const val KEY_COMMENT_CONTENT = "key_comment_content"
    }
}
