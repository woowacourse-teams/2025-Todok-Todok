package com.team.todoktodok.presentation.xml.discussions

import com.team.domain.model.Discussion
import com.team.domain.model.active.ActivatedDiscussionPage
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.todoktodok.presentation.xml.discussions.all.AllDiscussionsUiState
import com.team.todoktodok.presentation.compose.discussion.hot.HotDiscussionUiState
import com.team.todoktodok.presentation.xml.discussions.my.MyDiscussionUiState

data class DiscussionsUiState(
    val hotDiscussion: HotDiscussionUiState = HotDiscussionUiState(),
    val myDiscussion: MyDiscussionUiState = MyDiscussionUiState(),
    val allDiscussionsUiState: AllDiscussionsUiState = AllDiscussionsUiState(),
) {
    fun refreshLatestDiscussion(): DiscussionsUiState = copy(allDiscussionsUiState = allDiscussionsUiState.refreshLatestDiscussion())

    fun addSearchDiscussion(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): DiscussionsUiState =
        copy(
            allDiscussionsUiState =
                allDiscussionsUiState.addSearchDiscussion(
                    keyword,
                    newDiscussions,
                ),
        )

    fun addHotDiscussion(
        newItems: List<Discussion>,
        activatedDiscussion: ActivatedDiscussionPage,
    ): DiscussionsUiState = copy(hotDiscussion = hotDiscussion.addHotDiscussions(newItems, activatedDiscussion))

    fun appendActivatedDiscussion(page: ActivatedDiscussionPage): DiscussionsUiState =
        copy(hotDiscussion = hotDiscussion.appendActivatedDiscussion(page))

    fun addMyDiscussion(
        createdDiscussion: List<Discussion>,
        participatedDiscussion: List<Discussion>,
    ): DiscussionsUiState = copy(myDiscussion = myDiscussion.add(createdDiscussion, participatedDiscussion))

    fun addLatestDiscussion(page: LatestDiscussionPage): DiscussionsUiState =
        copy(allDiscussionsUiState = allDiscussionsUiState.addLatestDiscussion(page))

    fun modifyDiscussion(discussion: Discussion): DiscussionsUiState {
        val newHotDiscussion = hotDiscussion.modifyDiscussion(discussion)
        val newAllDiscussionsUiState = allDiscussionsUiState.modifyAllDiscussion(discussion)
        return copy(
            hotDiscussion = newHotDiscussion,
            allDiscussionsUiState = newAllDiscussionsUiState,
        )
    }

    fun clearSearchDiscussion() = copy(allDiscussionsUiState = allDiscussionsUiState.clearSearchDiscussion())

    fun removeDiscussion(discussionId: Long): DiscussionsUiState {
        val newHotDiscussion = hotDiscussion.removeDiscussion(discussionId)
        val newMyDiscussion = myDiscussion.removeDiscussion(discussionId)
        val newAllDiscussionsUiState = allDiscussionsUiState.removeAllDiscussion(discussionId)
        return copy(newHotDiscussion, newMyDiscussion, newAllDiscussionsUiState)
    }

    val latestPageHasNext get() = allDiscussionsUiState.latestPageHasNext
    val latestPageNextCursor get() = allDiscussionsUiState.latestPageNextCursor
}
