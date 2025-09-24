package com.team.todoktodok.presentation.compose.discussion.search

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.component.DiscussionCardType
import com.team.todoktodok.presentation.xml.discussions.DiscussionUiState

data class SearchDiscussionsUiState(
    val discussions: List<DiscussionUiState> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.QueryHighlighting,
    val searchKeyword: String = EMPTY_SEARCH_KEYWORD,
) {
    fun add(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): SearchDiscussionsUiState {
        val updatedList = discussions.toMutableList()
        val discussion = newDiscussions.map { DiscussionUiState(it, searchKeyword = keyword) }
        updatedList.addAll(discussion)

        return copy(discussions = updatedList, searchKeyword = keyword)
    }

    fun clear() = copy(discussions = emptyList(), searchKeyword = EMPTY_SEARCH_KEYWORD)

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
