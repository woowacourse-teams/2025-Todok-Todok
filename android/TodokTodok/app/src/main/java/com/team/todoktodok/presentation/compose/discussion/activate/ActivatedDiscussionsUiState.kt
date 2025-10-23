package com.team.todoktodok.presentation.compose.discussion.activate

import com.team.domain.model.DiscussionPage
import com.team.domain.model.PageInfo
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiModel

data class ActivatedDiscussionsUiState(
    val discussions: List<DiscussionUiModel> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.Default,
    val pageInfo: PageInfo = PageInfo.EMPTY,
) {
    fun append(page: DiscussionPage): ActivatedDiscussionsUiState =
        copy(
            discussions = discussions + page.discussions.map { DiscussionUiModel(it) },
            pageInfo = page.pageInfo,
        )

    fun clear() = copy(discussions = emptyList(), pageInfo = PageInfo.EMPTY)

    val notHasDiscussion = discussions.isEmpty()
    val hasNextPage get() = pageInfo.hasNext
}
