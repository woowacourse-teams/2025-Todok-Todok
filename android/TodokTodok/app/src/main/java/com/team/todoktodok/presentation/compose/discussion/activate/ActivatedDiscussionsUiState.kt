package com.team.todoktodok.presentation.compose.discussion.activate

import com.team.domain.model.Discussion
import com.team.domain.model.active.ActivatedDiscussionPage
import com.team.domain.model.latest.PageInfo
import com.team.todoktodok.presentation.compose.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState

data class ActivatedDiscussionsUiState(
    val discussions: List<DiscussionUiState> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.Default,
    val pageInfo: PageInfo? = null,
) {
    fun update(page: ActivatedDiscussionPage): ActivatedDiscussionsUiState =
        copy(
            discussions = page.data.map { DiscussionUiState(it) },
            pageInfo = page.pageInfo,
        )

    fun append(page: ActivatedDiscussionPage): ActivatedDiscussionsUiState =
        copy(
            discussions = discussions + page.data.map { DiscussionUiState(it) },
            pageInfo = page.pageInfo,
        )

    fun remove(discussionId: Long): ActivatedDiscussionsUiState = copy(discussions = discussions.filter { it.discussionId != discussionId })

    fun modify(discussion: Discussion): ActivatedDiscussionsUiState =
        copy(
            discussions =
                discussions.map {
                    if (it.discussionId == discussion.id) DiscussionUiState(discussion) else it
                },
        )
}
