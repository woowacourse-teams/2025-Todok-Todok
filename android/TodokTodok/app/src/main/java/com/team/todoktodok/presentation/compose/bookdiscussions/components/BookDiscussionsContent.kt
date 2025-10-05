package com.team.todoktodok.presentation.compose.bookdiscussions.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.common.collect.ImmutableList
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDetailUiState
import com.team.todoktodok.presentation.compose.bookdiscussions.model.DiscussionItem

@Composable
fun BookDiscussionsContent(
    bookDetailUiState: BookDetailUiState,
    discussionItems: ImmutableList<DiscussionItem>,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        BookDetailSection(bookDetailUiState, Modifier)
        BookDiscussionsSection(discussionItems, Modifier)
    }
}

@Preview
@Composable
private fun BookContentPreview() {
}
