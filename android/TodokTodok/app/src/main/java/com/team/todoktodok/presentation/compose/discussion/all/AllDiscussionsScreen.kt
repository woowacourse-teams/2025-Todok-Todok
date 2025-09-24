package com.team.todoktodok.presentation.compose.discussion.all

import androidx.compose.runtime.Composable
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
            )
        }

        AllDiscussionMode.SEARCH -> {
            SearchDiscussionScreen(
                uiState = uiState.searchDiscussion,
                onClick = { discussionId ->
                    onClick(discussionId)
                },
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
