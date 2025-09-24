package com.team.todoktodok.presentation.compose.discussion.created

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.component.DiscussionCardType
import com.team.todoktodok.presentation.xml.discussions.DiscussionUiState

data class CreatedDiscussionsUiState(
    val discussions: List<DiscussionUiState> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.WriterHidden,
) {
    fun add(discussions: List<Discussion>): CreatedDiscussionsUiState {
        val newDiscussions =
            discussions
                .takeLast(MY_DISCUSSION_SIZE)
                .map { DiscussionUiState(it) }
                .reversed()
        return copy(discussions = newDiscussions)
    }

    fun remove(discussionId: Long): CreatedDiscussionsUiState = copy(discussions = discussions.filter { it.discussionId != discussionId })

    fun isEmpty(): Boolean = discussions.isEmpty()

    companion object {
        private const val MY_DISCUSSION_SIZE = 3
    }
}
