package com.team.todoktodok.presentation.compose.discussion.all

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsScreen
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionScreen
import com.team.todoktodok.presentation.compose.preview.AllDiscussionUiStatePreviewParameterProvider

@Composable
fun AllDiscussionsScreen(
    uiState: AllDiscussionsUiState,
    onLoadMore: () -> Unit,
    onClick: (Long) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState.mode) {
        AllDiscussionMode.LATEST -> {
            LatestDiscussionsScreen(
                onLoadMore = { onLoadMore() },
                uiState = uiState.latestDiscussion,
                isRefreshing = uiState.latestDiscussion.isRefreshing,
                onRefresh = { onRefresh() },
                onClick = { discussionId ->
                    onClick(discussionId)
                },
                modifier = modifier,
            )
        }

        AllDiscussionMode.SEARCH -> {
            SearchDiscussionScreen(
                uiState = uiState.searchDiscussion,
                onClick = { discussionId ->
                    onClick(discussionId)
                },
                modifier = modifier,
            )
        }
    }
}

@Preview
@Composable
fun AllDiscussionsScreenPreview(
    @PreviewParameter(AllDiscussionUiStatePreviewParameterProvider::class)
    uiState: AllDiscussionsUiState,
) {
    AllDiscussionsScreen(
        uiState = uiState,
        onLoadMore = {},
        onClick = {},
        onRefresh = {},
    )
}
