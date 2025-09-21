package com.team.todoktodok.presentation.compose.discussion

import com.team.domain.model.Discussion
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsUiState

data class DiscussionsUiState(
    val latestDiscussion: LatestDiscussionsUiState = LatestDiscussionsUiState(),
) {
    fun addLatestDiscussion(page: LatestDiscussionPage): DiscussionsUiState = copy(latestDiscussion = latestDiscussion.append(page))

    fun modifyDiscussion(discussion: Discussion): DiscussionsUiState {
        val newLatestDiscussion = latestDiscussion.modifyDiscussion(discussion)
        return copy(
            latestDiscussion = newLatestDiscussion,
        )
    }

    fun clearLatestDiscussion() = copy(latestDiscussion = latestDiscussion.clear())

    fun removeDiscussion(discussionId: Long): DiscussionsUiState {
        val newLatestDiscussion = latestDiscussion.removeDiscussion(discussionId)
        return copy(newLatestDiscussion)
    }

    val latestPageHasNext get() = latestDiscussion.hasNext
    val latestPageNextCursor get() = latestDiscussion.nextCursor
}
