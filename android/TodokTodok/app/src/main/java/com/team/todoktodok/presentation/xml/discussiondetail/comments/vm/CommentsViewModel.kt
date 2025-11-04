package com.team.todoktodok.presentation.xml.discussiondetail.comments.vm

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.core.event.MutableSingleLiveData
import com.team.core.event.SingleLiveData
import com.team.domain.model.exception.NetworkResult
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.xml.discussiondetail.comments.CommentsUiEvent
import com.team.todoktodok.presentation.xml.discussiondetail.comments.CommentsUiState
import com.team.todoktodok.presentation.xml.discussiondetail.model.CommentItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val commentRepository: CommentRepository,
        private val tokenRepository: TokenRepository,
    ) : ViewModel() {
        val discussionId: Long =
            savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: error("discussionId missing")

        private val _uiState = MutableLiveData(CommentsUiState().copy(isLoading = true))
        val uiState: LiveData<CommentsUiState> = _uiState

        private val _uiEvent = MutableSingleLiveData<CommentsUiEvent>()
        val uiEvent: SingleLiveData<CommentsUiEvent> = _uiEvent

        var commentsRvState: Parcelable? = null

        init {
            reloadComments()
        }

        fun loadCommentsShowState(showState: Parcelable?) {
            commentsRvState = showState
        }

        fun reloadComments() {
            viewModelScope.launch { loadComments() }
        }

        fun showNewComment() =
            viewModelScope.launch {
                loadComments()
                onUiEvent(CommentsUiEvent.ShowNewComment)
            }

        fun toggleLike(commentId: Long) {
            val prevState = _uiState.value ?: return
            val targetItem = prevState.comments.find { it.comment.id == commentId } ?: return
            val initialLikeCount = targetItem.comment.likeCount

            _uiState.value = applyOptimisticToggle(prevState, targetItem)
            scheduleCoalescedToggle(commentId, initialLikeCount)
        }

        private fun updateCommentItem(
            uiState: CommentsUiState,
            commentId: Long,
            update: (CommentItemUiState) -> CommentItemUiState,
        ): CommentsUiState =
            uiState.copy(
                comments =
                    uiState.comments.map { item ->
                        if (item.comment.id == commentId) update(item) else item
                    },
            )

        private fun applyOptimisticToggle(
            prevState: CommentsUiState,
            targetItem: CommentItemUiState,
        ): CommentsUiState {
            val isLikedAfterToggle = !targetItem.comment.isLikedByMe
            val likeCountStep = if (isLikedAfterToggle) 1 else -1

            return updateCommentItem(prevState, targetItem.comment.id) { item ->
                item.itemJob?.cancel()
                item.copy(
                    comment =
                        item.comment.copy(
                            isLikedByMe = isLikedAfterToggle,
                            likeCount = (item.comment.likeCount + likeCountStep).coerceAtLeast(0),
                        ),
                    itemJob = null,
                )
            }
        }

        private fun scheduleCoalescedToggle(
            commentId: Long,
            initialLikeCount: Int,
        ) {
            val coalescedJob =
                viewModelScope.launch {
                    delay(250)
                    if (!shouldSendToggle(commentId, initialLikeCount)) return@launch

                    handleResult(commentRepository.toggleLike(discussionId, commentId)) {
                        loadComment(commentId)
                    }
                }

            _uiState.value =
                _uiState.value?.let { current ->
                    updateCommentItem(current, commentId) { item -> item.copy(itemJob = coalescedJob) }
                }
        }

        private fun shouldSendToggle(
            commentId: Long,
            initialLikeCount: Int,
        ): Boolean {
            val currentLikeCount =
                _uiState.value
                    ?.comments
                    ?.find { it.comment.id == commentId }
                    ?.comment
                    ?.likeCount
                    ?: return false

            return currentLikeCount != initialLikeCount
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

        fun reportComment(
            commentId: Long,
            reason: String,
        ) = viewModelScope.launch {
            handleResult(
                commentRepository.report(discussionId, commentId, reason),
            ) { onUiEvent(CommentsUiEvent.ShowReportCommentSuccessMessage) }
        }

        fun showCommentCreate() {
            onUiEvent(
                CommentsUiEvent.ShowCommentCreate(
                    discussionId,
                    _uiState.value?.commentContent.orEmpty(),
                ),
            )
        }

        fun createComment() {
            viewModelScope.launch {
                handleResult(
                    commentRepository.saveComment(
                        discussionId,
                        _uiState.value?.commentContent.orEmpty(),
                    ),
                ) {
                    loadComments()
                    _uiState.value = _uiState.value?.copy(commentContent = "")
                }
            }
        }

        fun updateCommentContent(content: String) {
            _uiState.value = _uiState.value?.copy(commentContent = content)
        }

        fun navigateToOtherUserProfile(
            memberId: Long,
            memberName: String,
        ) {
            viewModelScope.launch {
                val isWithdrewMemberName = memberName == WITHDREW_MEMBER_NAME
                val isMyId = memberId == tokenRepository.getMemberId()
                if (!isMyId && !isWithdrewMemberName) {
                    onUiEvent(
                        CommentsUiEvent.NavigateToProfile(
                            memberId,
                        ),
                    )
                }
            }
        }

        private suspend fun loadComment(commentId: Long) {
            handleResult(commentRepository.getComment(discussionId, commentId)) { comment ->
                val state = _uiState.value ?: return@handleResult
                val comments = state.comments

                val idx = comments.indexOfFirst { it.comment.id == comment.id }
                if (idx == -1) return@handleResult

                val updatedItem = comments[idx].copy(comment = comment)
                val updatedList = comments.toMutableList().apply { this[idx] = updatedItem }

                _uiState.value = state.copy(comments = updatedList)
            }
        }

        private suspend fun loadComments() {
            handleResult(commentRepository.getCommentsByDiscussionId(discussionId)) { list ->
                val myId = tokenRepository.getMemberId()
                val items = list.map { CommentItemUiState(it, isMyComment = myId == it.writer.id) }
                _uiState.value = _uiState.value?.copy(comments = items, isLoading = false)
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
                is NetworkResult.Failure -> {
                    onUiEvent(CommentsUiEvent.ShowError(result.exception))
                    _uiState.value = _uiState.value?.copy(isLoading = false)
                }
            }
        }

        companion object {
            private const val WITHDREW_MEMBER_NAME = "(알수없음)"

            const val KEY_DISCUSSION_ID = "discussionId"
        }
    }
