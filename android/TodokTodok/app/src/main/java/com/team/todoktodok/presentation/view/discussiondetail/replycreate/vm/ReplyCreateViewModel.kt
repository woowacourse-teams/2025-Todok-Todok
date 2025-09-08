package com.team.todoktodok.presentation.view.discussiondetail.replycreate.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.repository.ReplyRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussiondetail.replycreate.ReplyCreateState
import com.team.todoktodok.presentation.view.discussiondetail.replycreate.ReplyCreateUiEvent
import kotlinx.coroutines.launch

class ReplyCreateViewModel(
    savedStateHandle: SavedStateHandle,
    private val replyRepository: ReplyRepository,
) : ViewModel() {
    private val discussionId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

    private val commentId =
        savedStateHandle.get<Long>(KEY_COMMENT_ID) ?: throw IllegalStateException()

    private val replyCreateState =
        ReplyCreateState.create(savedStateHandle.get<Long>(KEY_REPLY_ID))

    val content = savedStateHandle.get<String>(KEY_REPLY_CONTENT)

    private val _uiEvent = MutableSingleLiveData<ReplyCreateUiEvent>()
    val uiEvent: SingleLiveData<ReplyCreateUiEvent> = _uiEvent

    private val _replyContent = MutableLiveData("")
    val replyContent: LiveData<String> = _replyContent

    init {
        initReplyContent()
    }

    private fun initReplyContent() {
        when (replyCreateState) {
            ReplyCreateState.Create -> onReplyChanged(content.orEmpty())
            is ReplyCreateState.Update -> onReplyChanged(content.orEmpty())
        }
    }

    fun onReplyChanged(text: CharSequence?) {
        _replyContent.value = text?.toString().orEmpty()
    }

    fun submitReply() {
        viewModelScope.launch {
            when (replyCreateState) {
                ReplyCreateState.Create -> {
                    handleResult(
                        replyRepository.saveReply(
                            discussionId,
                            commentId,
                            replyContent.value?.trim().orEmpty(),
                        ),
                    ) { _uiEvent.setValue(ReplyCreateUiEvent.CreateReply) }
                }

                is ReplyCreateState.Update -> {
                    handleResult(
                        replyRepository.updateReply(
                            discussionId,
                            commentId,
                            replyCreateState.replyId,
                            replyContent.value?.trim().orEmpty(),
                        ),
                    ) { _uiEvent.setValue(ReplyCreateUiEvent.CreateReply) }
                }
            }
        }
    }

    fun saveReply() {
        when (replyCreateState) {
            ReplyCreateState.Create -> {
                _uiEvent.setValue(
                    ReplyCreateUiEvent.SaveContent(
                        _replyContent.value?.trim().orEmpty(),
                    ),
                )
            }

            is ReplyCreateState.Update -> Unit
        }
    }

    private fun onUiEvent(event: ReplyCreateUiEvent) {
        _uiEvent.setValue(event)
    }

    private inline fun <T> handleResult(
        result: NetworkResult<T>,
        onSuccess: (T) -> Unit,
    ) {
        when (result) {
            is NetworkResult.Success -> onSuccess(result.data)
            is NetworkResult.Failure -> onUiEvent(ReplyCreateUiEvent.ShowErrorMessage(result.exception))
        }
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussion_id"
        const val KEY_COMMENT_ID = "comment_id"
        const val KEY_REPLY_ID = "reply_id"
        const val KEY_REPLY_CONTENT = "reply_content"
    }
}
