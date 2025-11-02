package com.team.todoktodok.presentation.xml.discussiondetail.commentdetail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.exception.NetworkResult
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.ReplyRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.xml.discussiondetail.commentdetail.CommentDetailUiEvent
import com.team.todoktodok.presentation.xml.discussiondetail.commentdetail.CommentDetailUiState
import com.team.todoktodok.presentation.xml.discussiondetail.model.CommentItemUiState
import com.team.todoktodok.presentation.xml.discussiondetail.model.ReplyItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val commentRepository: CommentRepository,
        private val replyRepository: ReplyRepository,
        private val tokenRepository: TokenRepository,
    ) : ViewModel() {
        val discussionId =
            savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()

        val commentId =
            savedStateHandle.get<Long>(KEY_COMMENT_ID) ?: throw IllegalStateException()

        private val _uiState = MutableLiveData(CommentDetailUiState().copy(isLoading = true))
        val uiState: LiveData<CommentDetailUiState> = _uiState

        private val _uiEvent = MutableSingleLiveData<CommentDetailUiEvent>()
        val uiEvent: SingleLiveData<CommentDetailUiEvent> = _uiEvent

        init {
            viewModelScope.launch {
                loadComment()
                loadReplies()
            }
        }

        fun reloadContents() {
            viewModelScope.launch {
                loadComment()
                loadReplies()
            }
        }

        fun reloadReplies() {
            viewModelScope.launch {
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
                handleResult(
                    commentRepository.deleteComment(discussionId, commentId),
                ) {
                    _uiEvent.setValue(CommentDetailUiEvent.DeleteComment)
                }
            }
        }

        fun reportReply(
            replyId: Long,
            reason: String,
        ) {
            viewModelScope.launch {
                handleResult(replyRepository.report(discussionId, commentId, replyId, reason)) {
                    onUiEvent(CommentDetailUiEvent.ShowReportReplySuccessMessage)
                }
            }
        }

        fun reportComment(reason: String) {
            viewModelScope.launch {
                handleResult(
                    commentRepository.report(discussionId, commentId, reason),
                ) { onUiEvent(CommentDetailUiEvent.ShowReportCommentSuccessMessage) }
            }
        }

        fun toggleReplyLike(replyId: Long) {
            val prevState = _uiState.value ?: return
            val targetItem = prevState.replyItems.find { it.reply.replyId == replyId } ?: return
            val initialLikeCount = targetItem.reply.likeCount

            _uiState.value = applyOptimisticReplyToggle(prevState, targetItem)
            scheduleCoalescedReplyToggle(replyId, initialLikeCount)
        }

        private fun updateReplyItem(
            uiState: CommentDetailUiState,
            replyId: Long,
            update: (ReplyItemUiState) -> ReplyItemUiState,
        ): CommentDetailUiState =
            uiState.copy(
                replyItems =
                    uiState.replyItems.map { item -> if (item.reply.replyId == replyId) update(item) else item },
            )

        private fun applyOptimisticReplyToggle(
            prevState: CommentDetailUiState,
            replyItemUiState: ReplyItemUiState,
        ): CommentDetailUiState {
            val isLikedAfterToggle = !replyItemUiState.reply.isLikedByMe
            val likeCountStep = if (isLikedAfterToggle) 1 else -1

            return updateReplyItem(prevState, replyItemUiState.reply.replyId) { item ->
                item.itemJob?.cancel()
                item.copy(
                    reply =
                        item.reply.copy(
                            isLikedByMe = isLikedAfterToggle,
                            likeCount = (item.reply.likeCount + likeCountStep).coerceAtLeast(0),
                        ),
                    itemJob = null,
                )
            }
        }

        private fun scheduleCoalescedReplyToggle(
            replyId: Long,
            initialLikeCount: Int,
        ) {
            val coalescedJob =
                viewModelScope.launch {
                    delay(250)
                    if (!shouldSendReplyToggle(replyId, initialLikeCount)) return@launch
                    handleResult(replyRepository.toggleLike(discussionId, commentId, replyId)) {
                        loadReplies()
                    }
                }

            _uiState.value =
                _uiState.value?.let { current ->
                    updateReplyItem(current, replyId) { item -> item.copy(itemJob = coalescedJob) }
                }
        }

        private fun shouldSendReplyToggle(
            replyId: Long,
            initialLikeCount: Int,
        ): Boolean {
            val currentLikeCount =
                _uiState.value
                    ?.replyItems
                    ?.find { it.reply.replyId == replyId }
                    ?.reply
                    ?.likeCount
                    ?: return false

            return currentLikeCount != initialLikeCount
        }

        fun toggleCommentLike() {
            val prevState = _uiState.value ?: return
            val targetItem = prevState.commentItem
            val initialLikeCount = targetItem.comment.likeCount

            _uiState.value = applyOptimisticCommentToggle(prevState, targetItem)
            scheduleCoalescedCommentToggle(initialLikeCount)
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
                        CommentDetailUiEvent.NavigateToProfile(
                            memberId,
                        ),
                    )
                }
            }
        }

        private fun applyOptimisticCommentToggle(
            prevState: CommentDetailUiState,
            commentItemUiState: CommentItemUiState,
        ): CommentDetailUiState {
            val isLikedAfterToggle = !commentItemUiState.comment.isLikedByMe
            val likeCountStep = if (isLikedAfterToggle) 1 else -1

            return updateCommentItem(prevState) { item ->
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

        private fun scheduleCoalescedCommentToggle(initialLikeCount: Int) {
            val coalescedJob =
                viewModelScope.launch {
                    delay(250)
                    if (!shouldSendCommentToggle(initialLikeCount)) return@launch
                    handleResult(commentRepository.toggleLike(discussionId, commentId)) {
                        loadComment()
                        _uiEvent.setValue(CommentDetailUiEvent.ToggleCommentLike)
                    }
                }

            _uiState.value =
                _uiState.value?.let { current ->
                    updateCommentItem(current) { item -> item.copy(itemJob = coalescedJob) }
                }
        }

        private fun updateCommentItem(
            uiState: CommentDetailUiState,
            update: (CommentItemUiState) -> CommentItemUiState,
        ): CommentDetailUiState =
            uiState.copy(
                commentItem = update(uiState.commentItem),
            )

        private fun shouldSendCommentToggle(initialLikeCount: Int): Boolean {
            val currentLikeCount =
                _uiState.value
                    ?.commentItem
                    ?.comment
                    ?.likeCount
                    ?: return false

            return currentLikeCount != initialLikeCount
        }

        fun showReplyCreate() {
            val content = _uiState.value?.content ?: ""
            _uiEvent.setValue(CommentDetailUiEvent.ShowReplyCreate(discussionId, commentId, content))
        }

        fun createReply() {
            viewModelScope.launch {
                handleResult(
                    replyRepository.saveReply(
                        discussionId,
                        commentId,
                        _uiState.value?.content.orEmpty(),
                    ),
                ) {
                    loadReplies()
                    _uiState.value = _uiState.value?.copy(content = "")
                }
            }
        }

        private fun showNewReply() {
            _uiEvent.setValue(CommentDetailUiEvent.ShowNewReply)
        }

        private suspend fun loadComment() {
            val currentUiState = _uiState.value
            handleResult(
                commentRepository.getComment(discussionId, commentId),
            ) { comment ->
                val isMyComment = comment.writer.id == tokenRepository.getMemberId()
                _uiState.value =
                    currentUiState?.copy(
                        commentItem =
                            CommentItemUiState(
                                comment,
                                isMyComment,
                            ),
                        isLoading = false,
                    )
            }
        }

        private suspend fun loadReplies() {
            val currentUiState = _uiState.value
            val memberId = tokenRepository.getMemberId()
            handleResult(
                replyRepository.getReplies(discussionId, commentId),
            ) { replies ->
                val replyItems =
                    replies.map { reply ->
                        ReplyItemUiState(
                            reply,
                            reply.writer.id == memberId,
                        )
                    }
                _uiState.value = currentUiState?.copy(replyItems = replyItems, isLoading = false)
            }
        }

        private fun onUiEvent(event: CommentDetailUiEvent) {
            _uiEvent.setValue(event)
        }

        private inline fun <T> handleResult(
            result: NetworkResult<T>,
            onSuccess: (T) -> Unit,
        ) {
            when (result) {
                is NetworkResult.Success -> onSuccess(result.data)
                is NetworkResult.Failure -> {
                    onUiEvent(CommentDetailUiEvent.ShowError(result.exception))
                    _uiState.value = _uiState.value?.copy(isLoading = false)
                }
            }
        }

        companion object {
            private const val WITHDREW_MEMBER_NAME = "(알수없음)"

            const val KEY_DISCUSSION_ID = "discussion_id"
            const val KEY_COMMENT_ID = "comment_id"
        }
    }
