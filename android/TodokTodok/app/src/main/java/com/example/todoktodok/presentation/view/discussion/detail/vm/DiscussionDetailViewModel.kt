package com.example.todoktodok.presentation.view.discussion.detail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.domain.model.Comment
import com.example.domain.model.Discussion
import com.example.domain.model.member.Nickname
import com.example.domain.model.member.User
import com.example.domain.repository.CommentRepository
import com.example.domain.repository.DiscussionRepository
import com.example.todoktodok.presentation.core.event.MutableSingleLiveData
import com.example.todoktodok.presentation.core.event.SingleLiveData
import com.example.todoktodok.presentation.view.discussion.detail.DiscussionDetailUiEvent
import java.time.LocalDateTime

class DiscussionDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val discussionRepository: DiscussionRepository,
    private val commentRepository: CommentRepository,
) : ViewModel() {
    private val discussionRoomId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()
    private val _discussion = MutableLiveData<Discussion>()
    val discussion: LiveData<Discussion> = _discussion

    private val _comments = MutableLiveData<List<Comment>>(emptyList())
    val comments: LiveData<List<Comment>> = _comments

    private val _commentText = MutableLiveData("")
    val commentText: LiveData<String> = _commentText

    private val _uiEvent = MutableSingleLiveData<DiscussionDetailUiEvent>()
    val uiEvent: SingleLiveData<DiscussionDetailUiEvent> = _uiEvent

    init {
        loadDiscussionRoom()
        loadComments()
    }

    fun onUiEvent(uiEvent: DiscussionDetailUiEvent) {
        _uiEvent.setValue(uiEvent)
    }

    fun addComment(
        currentDateTime: LocalDateTime,
        content: String,
    ) {
        val comment =
            Comment(
                100,
                content,
                user,
                currentDateTime,
            )
        commentRepository.saveComment(comment)
    }

    fun onCommentChanged(text: CharSequence?) {
        _commentText.value = text?.toString() ?: ""
    }

    private fun loadDiscussionRoom() {
        _discussion.value =
            discussionRepository.getDiscussion(discussionRoomId).getOrNull()
    }

    fun loadComments() {
        _comments.value = commentRepository.getCommentsByDiscussionRoomId(discussionRoomId)
    }

    companion object {
        private val user = User(1, Nickname("동전"))
        const val KEY_DISCUSSION_ID = "discussionId"
    }
}
