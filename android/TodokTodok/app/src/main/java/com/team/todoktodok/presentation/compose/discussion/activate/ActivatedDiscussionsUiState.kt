package com.team.todoktodok.presentation.compose.discussion.activate

import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionPage
import com.team.domain.model.PageInfo
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiModel

data class ActivatedDiscussionsUiState(
    val discussions: List<DiscussionUiModel> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.Default,
    val pageInfo: PageInfo = PageInfo.EMPTY,
) {
    val hasNextPage get() = pageInfo.hasNext

    val notHasDiscussion = discussions.isEmpty()

    fun append(page: DiscussionPage): ActivatedDiscussionsUiState =
        copy(
            discussions = (discussions + page.discussions.map { DiscussionUiModel(it) }).distinctBy { it.discussionId },
            pageInfo = page.pageInfo,
        )

    fun remove(discussionId: Long): ActivatedDiscussionsUiState = copy(discussions = discussions.filter { it.discussionId != discussionId })

    fun modify(discussion: Discussion): ActivatedDiscussionsUiState =
        copy(
            discussions =
                discussions.map {
                    if (it.discussionId == discussion.id) DiscussionUiModel(discussion) else it
                },
        )

    fun clear() = copy(discussions = emptyList(), pageInfo = PageInfo.EMPTY)
}
