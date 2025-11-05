package com.team.ui_compose.discussion.hot

import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionPage
import com.team.ui_compose.discussion.activate.ActivatedDiscussionsUiState
import com.team.ui_compose.discussion.popular.PopularDiscussionsUiState

data class HotDiscussionUiState(
    val popularDiscussions: PopularDiscussionsUiState = PopularDiscussionsUiState(),
    val activatedDiscussions: ActivatedDiscussionsUiState = ActivatedDiscussionsUiState(),
    val isRefreshing: Boolean = false,
) {
    val hasNextPage get() = activatedDiscussions.hasNextPage

    fun setPopularDiscussions(discussions: List<Discussion>): HotDiscussionUiState =
        copy(popularDiscussions = popularDiscussions.setDiscussion(discussions))

    fun appendActivatedDiscussion(page: DiscussionPage): HotDiscussionUiState =
        copy(activatedDiscussions = activatedDiscussions.append(page), isRefreshing = false)

    fun clearForRefresh(): HotDiscussionUiState =
        copy(
            popularDiscussions = popularDiscussions.clear(),
            activatedDiscussions = activatedDiscussions.clear(),
            isRefreshing = true,
        )
}
