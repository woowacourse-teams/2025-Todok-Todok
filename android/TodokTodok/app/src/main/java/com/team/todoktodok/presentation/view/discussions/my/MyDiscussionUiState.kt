package com.team.todoktodok.presentation.view.discussions.my

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.view.discussions.DiscussionUiState
import com.team.todoktodok.presentation.view.discussions.my.adapter.MyDiscussionItems

data class MyDiscussionUiState(
    val items: List<MyDiscussionItems> = emptyList(),
) {
    fun isEmpty() = items.isEmpty()

    fun add(
        createdDiscussion: List<Discussion>,
        participatedDiscussion: List<Discussion>,
    ): MyDiscussionUiState {
        val created = handleMyDiscussionForVisible(createdDiscussion)
        val participated = handleMyDiscussionForVisible(participatedDiscussion)

        val updatedList =
            buildList {
                if (created.isNotEmpty()) add(MyDiscussionItems.CreatedDiscussionItem(created))

                if (participated.isNotEmpty()) {
                    if (created.isNotEmpty()) add(MyDiscussionItems.DividerItem)
                    add(MyDiscussionItems.ParticipatedDiscussionItem(participated))
                }
            }

        return copy(updatedList)
    }

    private fun handleMyDiscussionForVisible(discussion: List<Discussion>): List<DiscussionUiState> =
        discussion
            .takeLast(MY_DISCUSSION_SIZE)
            .map { DiscussionUiState(it) }
            .reversed()

    companion object {
        private const val MY_DISCUSSION_SIZE = 3
    }
}
