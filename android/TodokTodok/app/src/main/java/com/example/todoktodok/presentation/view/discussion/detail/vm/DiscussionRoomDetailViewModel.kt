package com.example.todoktodok.presentation.view.discussion.detail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.domain.model.Comment
import com.example.domain.model.DiscussionRoom
import com.example.domain.repository.CommentRepository
import com.example.domain.repository.DiscussionRoomRepository

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

    init {
        loadDiscussionRoom()
        loadComments()
    }

    private fun loadDiscussionRoom() {
        _discussionRoom.value =
            discussionRoomRepository.getDiscussionRoom(discussionRoomId).getOrNull()
    }

    private fun loadComments() {
        _comments.value = commentRepository.getCommentsByDiscussionRoomId(discussionRoomId)
    }

    companion object {
        const val KEY_DISCUSSION_ID = "discussionId"
    }
}
