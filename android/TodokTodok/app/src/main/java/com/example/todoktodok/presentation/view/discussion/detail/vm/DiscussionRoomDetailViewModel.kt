package com.example.todoktodok.presentation.view.discussion.detail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.domain.model.Comment
import com.example.domain.model.DiscussionRoom
import com.example.domain.model.member.Nickname
import com.example.domain.model.member.User
import com.example.domain.repository.CommentRepository
import com.example.domain.repository.DiscussionRoomRepository
import com.example.todoktodok.presentation.core.event.MutableSingleLiveData
import com.example.todoktodok.presentation.core.event.SingleLiveData
import com.example.todoktodok.presentation.view.discussion.detail.DiscussionRoomDetailUiEvent
import java.time.LocalDateTime

class DiscussionRoomDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val discussionRoomRepository: DiscussionRoomRepository,
    private val commentRepository: CommentRepository,
) : ViewModel() {
    private val discussionRoomId =
        savedStateHandle.get<Long>(KEY_DISCUSSION_ID) ?: throw IllegalStateException()
    private val _discussionRoom = MutableLiveData<DiscussionRoom>()
    val discussionRoom: LiveData<DiscussionRoom> = _discussionRoom

    private val _comments = MutableLiveData<List<Comment>>(emptyList())
    val comments: LiveData<List<Comment>> = _comments

    private val _uiEvent = MutableSingleLiveData<DiscussionRoomDetailUiEvent>()
    val uiEvent: SingleLiveData<DiscussionRoomDetailUiEvent> = _uiEvent

    init {
        loadDiscussionRoom()
        loadComments()
    }

    fun onUiEvent(uiEvent: DiscussionRoomDetailUiEvent) {
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

    private fun loadDiscussionRoom() {
        _discussionRoom.value =
            discussionRoomRepository.getDiscussionRoom(discussionRoomId).getOrNull()
    }

    private fun loadComments() {
        _comments.value = commentRepository.getCommentsByDiscussionRoomId(discussionRoomId)
    }

    companion object {
        private val user = User(1, Nickname("동전"))
        const val KEY_DISCUSSION_ID = "discussionId"
    }
}
