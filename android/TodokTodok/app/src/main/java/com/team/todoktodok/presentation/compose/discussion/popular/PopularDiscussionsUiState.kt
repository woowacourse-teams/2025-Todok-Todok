package com.team.todoktodok.presentation.compose.discussion.popular

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiModel

data class PopularDiscussionsUiState(
    val discussions: List<DiscussionUiModel> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.OpinionVisible,
) {
    fun update(discussions: List<Discussion>): PopularDiscussionsUiState = copy(discussions = discussions.map { DiscussionUiModel(it) })

    fun remove(discussionId: Long): PopularDiscussionsUiState = copy(discussions = discussions.filter { it.discussionId != discussionId })

    fun modify(discussion: Discussion): PopularDiscussionsUiState =
        copy(
            discussions =
                discussions.map {
                    if (it.discussionId == discussion.id) DiscussionUiModel(discussion) else it
                },
        )

    fun clear(): PopularDiscussionsUiState = copy(discussions = emptyList())
}
