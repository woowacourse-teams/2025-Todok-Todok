package com.team.todoktodok.presentation.compose.discussion.participated

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.xml.discussions.DiscussionUiState

data class ParticipatedDiscussionsUiState(
    val list: List<DiscussionUiState> = emptyList(),
) {
    fun add(discussions: List<Discussion>): ParticipatedDiscussionsUiState {
        val newDiscussions =
            discussions
                .takeLast(MY_DISCUSSION_SIZE)
                .map { DiscussionUiState(it) }
                .reversed()
        return copy(list = newDiscussions)
    }

    fun remove(discussionId: Long): ParticipatedDiscussionsUiState = copy(list = list.filter { it.discussionId != discussionId })

    fun isEmpty(): Boolean = list.isEmpty()

    companion object {
        private const val MY_DISCUSSION_SIZE = 3
    }
}
