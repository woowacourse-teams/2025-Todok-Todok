package com.team.todoktodok.presentation.compose.discussion.latest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.team.todoktodok.presentation.compose.component.DiscussionCard
import com.team.todoktodok.presentation.compose.component.InfinityLazyColumn
import com.team.todoktodok.presentation.compose.preview.LatestDiscussionsPreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.compose.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestDiscussionsScreen(
    onLoadMore: () -> Unit,
    onClick: (Long) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    latestDiscussionsUiState: LatestDiscussionsUiState,
    modifier: Modifier = Modifier,
) {
    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        state = state,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = White,
                color = Green1A,
                state = state,
            )
        },
    ) {
        InfinityLazyColumn(
            loadMore = { onLoadMore() },
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            content = {
                items(
                    items = latestDiscussionsUiState.items,
                    key = { it.discussionId },
                ) { item ->
                    DiscussionCard(
                        uiState = item,
                        onClick = { onClick(item.discussionId) },
                        discussionCardType = latestDiscussionsUiState.type,
                    )
                }
            },
        )
    }
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
        isRefreshing = false,
        onRefresh = {},
        latestDiscussionsUiState = latestDiscussionsUiState,
    )
}
