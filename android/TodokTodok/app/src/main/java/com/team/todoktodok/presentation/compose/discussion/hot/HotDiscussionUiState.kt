package com.team.todoktodok.presentation.compose.discussion.hot

import com.team.domain.model.Discussion
import com.team.domain.model.active.ActivatedDiscussionPage
import com.team.todoktodok.presentation.compose.discussion.activate.ActivatedDiscussionsUiState
import com.team.todoktodok.presentation.compose.discussion.popular.PopularDiscussionsUiState

data class HotDiscussionUiState(
    val popularDiscussions: PopularDiscussionsUiState = PopularDiscussionsUiState(),
    val activatedDiscussions: ActivatedDiscussionsUiState = ActivatedDiscussionsUiState(),
) {
    fun addHotDiscussions(
        hotDiscussions: List<Discussion>,
        activatedDiscussion: ActivatedDiscussionPage,
    ): HotDiscussionUiState =
        copy(
            popularDiscussions = popularDiscussions.update(hotDiscussions),
            activatedDiscussions = activatedDiscussions.update(activatedDiscussion),
        )

    fun removeDiscussion(discussionId: Long): HotDiscussionUiState =
        copy(
            popularDiscussions = popularDiscussions.remove(discussionId),
            activatedDiscussions = activatedDiscussions.remove(discussionId),
        )

    fun appendActivatedDiscussion(page: ActivatedDiscussionPage): HotDiscussionUiState =
        copy(activatedDiscussions = activatedDiscussions.append(page))

    fun modifyDiscussion(discussion: Discussion): HotDiscussionUiState =
        copy(
            popularDiscussions = popularDiscussions.modify(discussion),
            activatedDiscussions = activatedDiscussions.modify(discussion),
        )
}
