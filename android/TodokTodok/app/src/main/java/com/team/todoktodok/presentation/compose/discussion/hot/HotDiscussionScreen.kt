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
import com.team.todoktodok.presentation.compose.component.DiscussionCard
import com.team.todoktodok.presentation.compose.component.InfinityLazyColumn
import com.team.todoktodok.presentation.compose.discussion.activate.ActivatedDiscussionHeader
import com.team.todoktodok.presentation.compose.discussion.popular.PopularDiscussionsScreen
import com.team.todoktodok.presentation.compose.preview.HotDiscussionPreviewParameterProvider

@Composable
fun HotDiscussionScreen(
    onLoadMore: () -> Unit,
    onClick: (Long) -> Unit,
    hotDiscussions: HotDiscussionUiState,
) {
    InfinityLazyColumn(
        loadMore = { onLoadMore() },
        loadMoreLimitCount = 3,
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        item {
            PopularDiscussionsScreen(
                onClick = onClick,
                uiState = hotDiscussions.popularDiscussions,
            )
        }

        item { ActivatedDiscussionHeader() }

        items(
            items = hotDiscussions.activatedDiscussions.discussions,
            key = { it.discussionId },
        ) { item ->
            DiscussionCard(
                uiState = item,
                onClick = { onClick(item.discussionId) },
                discussionCardType = hotDiscussions.activatedDiscussions.type,
                modifier = Modifier.padding(horizontal = 5.dp),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HotDiscissionContent(
    @PreviewParameter(HotDiscussionPreviewParameterProvider::class)
    hotDiscussionUiState: HotDiscussionUiState,
) {
    HotDiscussionScreen(
        onLoadMore = {},
        onClick = {},
        hotDiscussions = hotDiscussionUiState,
    )
}
