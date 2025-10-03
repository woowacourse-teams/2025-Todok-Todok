package com.team.todoktodok.presentation.compose.discussion.activate

import com.team.domain.model.Discussion
import com.team.domain.model.PageInfo
import com.team.domain.model.active.ActivatedDiscussionPage
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState

data class ActivatedDiscussionsUiState(
    val discussions: List<DiscussionUiState> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.Default,
    val pageInfo: PageInfo = PageInfo.EMPTY,
) {
    val hasNextPage get() = pageInfo.hasNext

    val notHasDiscussion = discussions.isEmpty()

    fun append(page: ActivatedDiscussionPage): ActivatedDiscussionsUiState =
        copy(
            discussions = (discussions + page.data.map { DiscussionUiState(it) }).distinctBy { it.discussionId },
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
