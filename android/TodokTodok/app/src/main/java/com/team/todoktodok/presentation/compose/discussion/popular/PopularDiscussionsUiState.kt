package com.team.todoktodok.presentation.compose.discussion.popular

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState

data class PopularDiscussionsUiState(
    val discussions: List<DiscussionUiState> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.OpinionVisible,
) {
    fun update(discussions: List<Discussion>): PopularDiscussionsUiState = copy(discussions = discussions.map { DiscussionUiState(it) })

    fun remove(discussionId: Long): PopularDiscussionsUiState = copy(discussions = discussions.filter { it.discussionId != discussionId })

    fun modify(discussion: Discussion): PopularDiscussionsUiState =
        copy(
            discussions =
                discussions.map {
                    if (it.discussionId == discussion.id) DiscussionUiState(discussion) else it
                },
        )
}
