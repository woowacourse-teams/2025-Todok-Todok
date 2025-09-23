package com.team.todoktodok.presentation.compose.discussion.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.team.todoktodok.presentation.compose.component.DiscussionCard
import com.team.todoktodok.presentation.compose.preview.SearchDiscussionsUiStatePreviewParameterProvider

@Composable
fun SearchDiscussionScreen(
    onClick: (Long) -> Unit,
    searchDiscussion: SearchDiscussionsUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier.padding(horizontal = 10.dp),
    ) {
        searchDiscussion.items.forEach { item ->
            DiscussionCard(
                uiState = item,
                discussionCardType = searchDiscussion.type,
                onClick = { onClick(item.discussionId) },
            )
        }
    }
}

@Preview
@Composable
fun SearchDiscussionScreenPreview(
    @PreviewParameter(SearchDiscussionsUiStatePreviewParameterProvider::class)
    searchDiscussion: SearchDiscussionsUiState,
) {
    SearchDiscussionScreen(
        onClick = {},
        searchDiscussion = searchDiscussion,
    )
}
