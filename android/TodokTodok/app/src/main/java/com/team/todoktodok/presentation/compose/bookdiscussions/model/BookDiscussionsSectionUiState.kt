package com.team.todoktodok.presentation.compose.bookdiscussions.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class BookDiscussionsSectionUiState(
    val isPagingLoading: Boolean,
    val discussionItems: ImmutableList<DiscussionItem>,
) {
    fun update(
        isPagingLoading: Boolean = this.isPagingLoading,
        discussionItems: List<DiscussionItem> = this.discussionItems,
    ) = this.copy(isPagingLoading, discussionItems.toImmutableList())

    fun addDiscussionItems(newDiscussionItems: List<DiscussionItem>): BookDiscussionsSectionUiState {
        val totalDiscussionItems = (discussionItems + newDiscussionItems).toImmutableList()
        return this.copy(discussionItems = totalDiscussionItems)
    }
}
