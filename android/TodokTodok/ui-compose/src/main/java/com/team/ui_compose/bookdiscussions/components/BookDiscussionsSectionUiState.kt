package com.team.ui_compose.bookdiscussions.components

import androidx.compose.runtime.Immutable
import com.team.ui_compose.bookdiscussions.model.DiscussionItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class BookDiscussionsSectionUiState(
    val isPagingLoading: Boolean = false,
    val discussionItems: ImmutableList<DiscussionItem> = persistentListOf(),
) {
    fun update(
        isPagingLoading: Boolean = this.isPagingLoading,
        discussionItems: List<DiscussionItem> = this.discussionItems,
    ) = this.copy(isPagingLoading, discussionItems.toImmutableList())

    fun addDiscussionItems(newDiscussionItems: List<DiscussionItem>): BookDiscussionsSectionUiState {
        val totalDiscussionItems =
            (discussionItems + newDiscussionItems).distinctBy { it.discussionId }.toImmutableList()
        return this.copy(discussionItems = totalDiscussionItems)
    }
}
