package com.team.todoktodok.presentation.compose.discussion.hot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.team.todoktodok.presentation.compose.core.component.DiscussionCard
import com.team.todoktodok.presentation.compose.core.component.InfinityLazyColumn
import com.team.todoktodok.presentation.compose.discussion.activate.ActivatedDiscussionHeader
import com.team.todoktodok.presentation.compose.discussion.popular.PopularDiscussionsScreen
import com.team.todoktodok.presentation.compose.preview.HotDiscussionPreviewParameterProvider

@Composable
fun HotDiscussionScreen(
    uiState: HotDiscussionUiState,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    InfinityLazyColumn(
        loadMore = { onLoadMore() },
        loadMoreLimitCount = 3,
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier.padding(10.dp),
    ) {
        item {
            PopularDiscussionsScreen(
                uiState = uiState.popularDiscussions,
            )
        }

        item { ActivatedDiscussionHeader() }

        items(
            items = uiState.activatedDiscussions.discussions,
            key = { it.discussionId },
        ) { item ->
            DiscussionCard(
                uiState = item,
                discussionCardType = uiState.activatedDiscussions.type,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun HotDiscissionContent(
    @PreviewParameter(HotDiscussionPreviewParameterProvider::class)
    hotDiscussionUiState: HotDiscussionUiState,
) {
    HotDiscussionScreen(
        onLoadMore = {},
        uiState = hotDiscussionUiState,
    )
}
