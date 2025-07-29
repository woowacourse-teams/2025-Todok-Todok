package com.team.todoktodok.presentation.utview.discussions

import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionFilter

data class DiscussionsUiState(
    val allDiscussions: List<Discussion> = emptyList(),
    val myDiscussions: List<Discussion> = emptyList(),
    val searchKeyword: String = "",
    val tab: DiscussionFilter = DiscussionFilter.ALL,
)
