package com.team.todoktodok.presentation.compose.discussion.my

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.discussion.created.CreatedDiscussionsUiState
import com.team.todoktodok.presentation.compose.discussion.participated.ParticipatedDiscussionsUiState

data class MyDiscussionUiState(
    val createdDiscussionsUiState: CreatedDiscussionsUiState = CreatedDiscussionsUiState(),
    val participatedDiscussionsUiState: ParticipatedDiscussionsUiState = ParticipatedDiscussionsUiState(),
) {
    fun isEmpty() = createdDiscussionsUiState.isEmpty() && participatedDiscussionsUiState.isEmpty()

    fun addCreatedDiscussions(discussions: List<Discussion>): MyDiscussionUiState =
        copy(createdDiscussionsUiState = createdDiscussionsUiState.add(discussions))

    fun addParticipatedDiscussions(discussions: List<Discussion>): MyDiscussionUiState =
        copy(participatedDiscussionsUiState = participatedDiscussionsUiState.add(discussions))

    fun removeDiscussion(discussionId: Long): MyDiscussionUiState =
        copy(
            createdDiscussionsUiState = createdDiscussionsUiState.remove(discussionId),
            participatedDiscussionsUiState = participatedDiscussionsUiState.remove(discussionId),
        )
}
