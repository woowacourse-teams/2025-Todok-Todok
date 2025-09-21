package com.team.todoktodok.presentation.xml.discussions.my

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.xml.discussions.DiscussionUiState
import com.team.todoktodok.presentation.xml.discussions.my.adapter.MyDiscussionItems

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

        val updatedList = makeDiscussionItems(created, participated)

        return copy(updatedList)
    }

    fun removeDiscussion(discussionId: Long): MyDiscussionUiState {
        val newCreatedItems = removeCreatedDiscussion(discussionId)
        val newParticipatedItems = removeParticipatedDiscussion(discussionId)
        val updatedList = makeDiscussionItems(newCreatedItems, newParticipatedItems)

        return copy(items = updatedList)
    }

    private fun removeCreatedDiscussion(discussionId: Long): List<DiscussionUiState> =
        items
            .filterIsInstance<MyDiscussionItems.CreatedDiscussionItem>()
            .firstOrNull()
            ?.items
            ?.filter { it.discussionId != discussionId } ?: emptyList()

    private fun removeParticipatedDiscussion(discussionId: Long): List<DiscussionUiState> =
        items
            .filterIsInstance<MyDiscussionItems.ParticipatedDiscussionItem>()
            .firstOrNull()
            ?.items
            ?.filter { it.discussionId != discussionId } ?: emptyList()

    private fun makeDiscussionItems(
        newCreatedItems: List<DiscussionUiState>,
        newParticipatedItems: List<DiscussionUiState>,
    ): List<MyDiscussionItems> =
        buildList {
            if (newCreatedItems.isNotEmpty()) {
                add(
                    MyDiscussionItems.CreatedDiscussionItem(
                        newCreatedItems,
                    ),
                )
            }

            if (newParticipatedItems.isNotEmpty()) {
                if (newCreatedItems.isNotEmpty()) add(MyDiscussionItems.DividerItem)
                add(MyDiscussionItems.ParticipatedDiscussionItem(newParticipatedItems))
            }
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
