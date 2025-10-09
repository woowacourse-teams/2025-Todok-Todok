package com.team.todoktodok.presentation.compose.bookdiscussions.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    Column(modifier.padding(horizontal = 16.dp)) {
        BookDetailSection(bookDetailUiState, Modifier)
        Spacer(Modifier.height(20.dp))
        BookDiscussionsSection(discussionItems, Modifier)
    }
}

@Preview
@Composable
private fun BookContentPreview() {
}
