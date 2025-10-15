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
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDetailSectionUiState
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDiscussionsSectionUiState

@SuppressLint("ComposeModifierReused")
@Composable
fun BookDiscussionsContent(
    bookDetailSectionUiState: BookDetailSectionUiState,
    bookDiscussionsSectionUiState: BookDiscussionsSectionUiState,
    loadMoreItems: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(horizontal = 16.dp)) {
        BookDetailSection(bookDetailSectionUiState, Modifier)
        Spacer(Modifier.height(20.dp))
        BookDiscussionsSection(bookDiscussionsSectionUiState, loadMoreItems, Modifier)
    }
}

@Preview
@Composable
private fun BookContentPreview() {
}
