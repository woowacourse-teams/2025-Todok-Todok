package com.team.todoktodok.presentation.compose.bookdiscussions.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class BookDiscussionsSectionUiState(
    val isPagingLoading: Boolean,
    val discussionItems: ImmutableList<DiscussionItem>,
)
