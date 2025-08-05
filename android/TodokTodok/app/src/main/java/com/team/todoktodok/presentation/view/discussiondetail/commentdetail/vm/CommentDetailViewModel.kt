package com.team.todoktodok.presentation.view.discussiondetail.commentdetail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.team.domain.model.Comment
import com.team.domain.model.Reply
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.domain.repository.CommentRepository
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.CommentDetailUiState
import java.time.LocalDateTime

class CommentDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val commentRepository: CommentRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData<CommentDetailUiState?>(null)
    val uiState: LiveData<CommentDetailUiState?> = _uiState

    init {
        loadComment()
        loadReplies()
    }

    private fun loadComment() {
        _uiState.value =
            CommentDetailUiState(
                Comment(
                    1,
                    "정말 좋은 글이에요!",
                    User(1, Nickname("동전")),
                    LocalDateTime.of(2024, 3, 15, 10, 30),
                ),
            )
    }

    private fun loadReplies() {
        _uiState.value =
            _uiState.value?.copy(
                replies =
                    listOf(
                        Reply(
                            content = "정말 좋은 글이에요!",
                            user = User(1, Nickname("동전")),
                            createdAt = LocalDateTime.of(2024, 3, 15, 10, 30),
                            replyId = 1,
                            likeCount = 0,
                        ),
                        Reply(
                            content = "정말 좋은 글이에요?",
                            user = User(2, Nickname("단전")),
                            createdAt = LocalDateTime.of(2024, 3, 15, 10, 30),
                            replyId = 2,
                            likeCount = 0,
                        ),
                    ),
            )
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussion_id"
        const val KEY_COMMENT_ID = "comment_id"
    }
}
