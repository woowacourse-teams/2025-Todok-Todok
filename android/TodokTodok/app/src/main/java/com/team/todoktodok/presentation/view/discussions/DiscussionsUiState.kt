package com.team.todoktodok.presentation.view.discussions

import com.team.domain.model.Discussion
import com.team.domain.model.active.ActivatedDiscussionPage
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.domain.model.latest.PageInfo
import com.team.todoktodok.presentation.view.discussions.hot.HotDiscussionUiState
import com.team.todoktodok.presentation.view.discussions.my.MyDiscussionUiState

data class DiscussionsUiState(
    val hotDiscussion: HotDiscussionUiState = HotDiscussionUiState(),
    val myDiscussion: MyDiscussionUiState = MyDiscussionUiState(),
    val latestDiscussions: Set<DiscussionUiState> = emptySet(),
    val latestPage: PageInfo = PageInfo.EMPTY,
    val searchKeyword: String = EMPTY_SEARCH_KEYWORD,
    val loadBySearch: Boolean = false,
    val isLoading: Boolean = false,
) {
    fun addSearchDiscussion(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): DiscussionsUiState {
        val discussion = newDiscussions.map { DiscussionUiState(it) }
        return copy(
            searchKeyword = keyword,
            latestDiscussions = discussion.toSet(),
            latestPage = PageInfo.EMPTY,
            loadBySearch = true,
        )
    }

    fun addHotDiscussion(
        newItems: List<Discussion>,
        activatedDiscussion: ActivatedDiscussionPage,
    ): DiscussionsUiState = copy(hotDiscussion = hotDiscussion.add(newItems, activatedDiscussion))

    fun appendActivatedDiscussion(page: ActivatedDiscussionPage): DiscussionsUiState =
        copy(hotDiscussion = hotDiscussion.appendActivatedDiscussion(page))

    fun addMyDiscussion(
        createdDiscussion: List<Discussion>,
        participatedDiscussion: List<Discussion>,
    ): DiscussionsUiState =
        copy(
            myDiscussion = myDiscussion.add(createdDiscussion, participatedDiscussion),
        )

    fun addLatestDiscussion(
        page: LatestDiscussionPage,
        reload: Boolean,
    ): DiscussionsUiState {
        val newDiscussion = latestDiscussions.toMutableList()
        if (loadBySearch) newDiscussion.clear()
        if (!reload) newDiscussion.clear()

        val discussion = page.discussions.map { it.toUiState() }
        val pageInfo = page.pageInfo

        newDiscussion.addAll(discussion)

        val newLatestPage = latestPage.copy(pageInfo.hasNext, pageInfo.nextCursor)

        return copy(
            latestDiscussions = newDiscussion.toSet(),
            latestPage = newLatestPage,
            loadBySearch = false,
        )
    }

    val latestPageHasNext get() = latestPage.hasNext

    fun toggleLoading() = copy(isLoading = !isLoading)

    companion object {
        private const val EMPTY_SEARCH_KEYWORD = ""
    }
}
