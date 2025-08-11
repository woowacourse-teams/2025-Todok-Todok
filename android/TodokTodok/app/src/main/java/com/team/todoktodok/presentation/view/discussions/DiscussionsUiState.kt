package com.team.todoktodok.presentation.view.discussions

import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionFilter

data class DiscussionsUiState(
    val allDiscussions: List<Discussion> = emptyList(),
    val myDiscussions: List<Discussion> = emptyList(),
    val searchKeyword: String = "",
    val filter: DiscussionFilter = DiscussionFilter.ALL,
    val isLoading: Boolean = true,
) {
    val allDiscussionsSize get() = allDiscussions.size
    val myDiscussionsSize get() = myDiscussions.size

    fun toggleLoading() = copy(isLoading = !isLoading)
}
