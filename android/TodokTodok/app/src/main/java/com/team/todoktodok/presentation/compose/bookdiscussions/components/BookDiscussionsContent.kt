package com.team.todoktodok.presentation.compose.bookdiscussions.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDetailUiState
import com.team.todoktodok.presentation.compose.bookdiscussions.model.DiscussionItem
import kotlinx.collections.immutable.ImmutableList

@SuppressLint("ComposeModifierReused")
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
