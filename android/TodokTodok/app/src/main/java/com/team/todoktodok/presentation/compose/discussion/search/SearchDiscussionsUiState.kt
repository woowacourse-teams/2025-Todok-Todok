package com.team.todoktodok.presentation.compose.discussion.search

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.component.DiscussionCardType
import com.team.todoktodok.presentation.xml.discussions.DiscussionUiState

data class SearchDiscussionsUiState(
    val items: List<DiscussionUiState> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.QueryHighlighting,
    val searchKeyword: String = EMPTY_SEARCH_KEYWORD,
) {
    fun add(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): SearchDiscussionsUiState {
        val updatedList = items.toMutableList()
        val discussion = newDiscussions.map { DiscussionUiState(it, searchKeyword = keyword) }
        updatedList.addAll(discussion)

        return copy(items = updatedList, searchKeyword = keyword)
    }

    fun clear() = copy(items = emptyList(), searchKeyword = EMPTY_SEARCH_KEYWORD)

    fun modifyDiscussion(newDiscussion: Discussion): SearchDiscussionsUiState =
        copy(
            items =
                items.map {
                    if (it.discussionId == newDiscussion.id) {
                        DiscussionUiState(newDiscussion)
                    } else {
                        it
                    }
                },
        )

    fun removeDiscussion(discussionId: Long): SearchDiscussionsUiState = copy(items = items.filter { it.discussionId != discussionId })

    companion object {
        private const val EMPTY_SEARCH_KEYWORD = ""
    }
}
