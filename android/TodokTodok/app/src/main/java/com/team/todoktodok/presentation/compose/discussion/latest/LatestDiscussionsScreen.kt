package com.team.todoktodok.presentation.compose.discussion.latest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.team.todoktodok.presentation.compose.component.DiscussionCard
import com.team.todoktodok.presentation.compose.component.InfinityLazyColumn
import com.team.todoktodok.presentation.compose.preview.LatestDiscussionsPreviewParameterProvider
import com.team.todoktodok.presentation.xml.discussions.latest.LatestDiscussionsUiState

@Composable
fun LatestDiscussionsScreen(
    onLoadMore: () -> Unit,
    onClick: (Long) -> Unit,
    latestDiscussionsUiState: LatestDiscussionsUiState,
    modifier: Modifier = Modifier,
) {
    InfinityLazyColumn(
        loadMore = { onLoadMore() },
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier.padding(horizontal = 10.dp),
        content = {
            items(
                items = latestDiscussionsUiState.items,
                key = { it.discussionId },
            ) { item ->
                DiscussionCard(
                    uiState = item,
                    onClick = { onClick(item.discussionId) },
                )
            }
        },
    )
}

@Preview
@Composable
fun DiscussionsScreenPreview(
    @PreviewParameter(LatestDiscussionsPreviewParameterProvider::class)
    latestDiscussionsUiState: LatestDiscussionsUiState,
) {
    LatestDiscussionsScreen(
        onLoadMore = {},
        onClick = {},
        latestDiscussionsUiState = latestDiscussionsUiState,
    )
}
