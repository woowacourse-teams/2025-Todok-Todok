package com.team.todoktodok.presentation.compose.discussion.search

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState

data class SearchDiscussionsUiState(
    val discussions: List<DiscussionUiState> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.QueryHighlighting,
    val searchKeyword: String = EMPTY_SEARCH_KEYWORD,
    val previousKeyword: String = EMPTY_SEARCH_KEYWORD,
) {
    fun add(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): SearchDiscussionsUiState {
        if (keyword == previousKeyword || keyword.isBlank()) return this
        val newDiscussions = newDiscussions.map { DiscussionUiState(it) }

        return copy(
            discussions = newDiscussions,
            searchKeyword = keyword,
            previousKeyword = keyword,
        )
    }

    fun clear() = copy(discussions = emptyList(), searchKeyword = EMPTY_SEARCH_KEYWORD)

    fun modifyKeyword(keyword: String) = copy(searchKeyword = keyword)

    fun modify(newDiscussion: Discussion): SearchDiscussionsUiState =
        copy(
            discussions =
                discussions.map {
                    if (it.discussionId == newDiscussion.id) {
                        DiscussionUiState(newDiscussion)
                    } else {
                        it
                    }
                },
        )

    fun remove(discussionId: Long): SearchDiscussionsUiState = copy(discussions = discussions.filter { it.discussionId != discussionId })

    companion object {
        private const val EMPTY_SEARCH_KEYWORD = ""
    }
}
