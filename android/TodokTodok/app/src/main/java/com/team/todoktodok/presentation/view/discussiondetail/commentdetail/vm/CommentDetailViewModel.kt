package com.team.todoktodok.presentation.view.discussiondetail.commentdetail.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.team.domain.repository.CommentRepository

class CommentDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val commentRepository: CommentRepository,
) : ViewModel() {
    companion object {
        const val KEY_DISCUSSION_ID = "discussion_id"
        const val KEY_COMMENT_ID = "comment_id"
    }
}
