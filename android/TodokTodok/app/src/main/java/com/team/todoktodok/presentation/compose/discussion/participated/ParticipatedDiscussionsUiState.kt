package com.team.todoktodok.presentation.compose.discussion.participated

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState

data class ParticipatedDiscussionsUiState(
    val discussions: List<DiscussionUiState> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.Default,
) {
    fun add(discussions: List<Discussion>): ParticipatedDiscussionsUiState {
        val newDiscussions =
            discussions
                .takeLast(MY_DISCUSSION_SIZE)
                .map { DiscussionUiState(it) }
                .reversed()
        return copy(discussions = newDiscussions)
    }

    fun remove(discussionId: Long): ParticipatedDiscussionsUiState =
        copy(discussions = discussions.filter { it.discussionId != discussionId })

    fun isEmpty(): Boolean = discussions.isEmpty()

    companion object {
        private const val MY_DISCUSSION_SIZE = 3
    }
}
