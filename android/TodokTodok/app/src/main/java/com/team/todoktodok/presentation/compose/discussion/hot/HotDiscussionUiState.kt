package com.team.todoktodok.presentation.compose.discussion.hot

import com.team.domain.model.Discussion
import com.team.domain.model.active.ActivatedDiscussionPage
import com.team.todoktodok.presentation.compose.discussion.activate.ActivatedDiscussionsUiState
import com.team.todoktodok.presentation.compose.discussion.popular.PopularDiscussionsUiState
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

data class HotDiscussionUiState(
    val popularDiscussions: PopularDiscussionsUiState = PopularDiscussionsUiState(),
    val activatedDiscussions: ActivatedDiscussionsUiState = ActivatedDiscussionsUiState(),
    val isRefreshing: Boolean = false,
) {
    val hasNextPage get() = activatedDiscussions.hasNextPage

    fun addPopularDiscussions(discussions: List<Discussion>): HotDiscussionUiState =
        copy(
            popularDiscussions = popularDiscussions.update(discussions),
        )

    fun removeDiscussion(discussionId: Long): HotDiscussionUiState =
        copy(
            popularDiscussions = popularDiscussions.remove(discussionId),
            activatedDiscussions = activatedDiscussions.remove(discussionId),
        )

    fun appendActivatedDiscussion(page: ActivatedDiscussionPage): HotDiscussionUiState =
        copy(activatedDiscussions = activatedDiscussions.append(page), isRefreshing = false)

    fun modifyDiscussion(discussion: SerializationDiscussion): HotDiscussionUiState {
        val newDiscussion = discussion.toDomain()
        return copy(
            popularDiscussions = popularDiscussions.modify(newDiscussion),
            activatedDiscussions = activatedDiscussions.modify(newDiscussion),
        )
    }

    fun clearForRefresh(): HotDiscussionUiState =
        copy(
            popularDiscussions = popularDiscussions.clear(),
            activatedDiscussions = activatedDiscussions.clear(),
            isRefreshing = true,
        )
}
