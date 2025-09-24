package com.team.todoktodok.presentation.compose.discussion.activate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.component.DiscussionCard
import com.team.todoktodok.presentation.compose.component.InfinityLazyColumn
import com.team.todoktodok.presentation.compose.preview.ActivatedDiscussionsUiStatePreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.Black18
import com.team.todoktodok.presentation.compose.theme.Pretendard

@Composable
fun ActivatedDiscussionScreen(
    onLoadMore: () -> Unit,
    onClick: (Long) -> Unit,
    activatedDiscussions: ActivatedDiscussionsUiState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ActivatedDiscussionHeader()

        InfinityLazyColumn(
            loadMore = { onLoadMore() },
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier =
                Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 10.dp),
            content = {
                items(
                    items = activatedDiscussions.discussions,
                    key = { it.discussionId },
                ) { item ->
                    DiscussionCard(
                        uiState = item,
                        onClick = { onClick(item.discussionId) },
                        discussionCardType = activatedDiscussions.type,
                    )
                }
            },
        )
    }
}

@Composable
fun ActivatedDiscussionHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.img_fire),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )

        Text(
            text = stringResource(R.string.discussions_activated_discussion_rooms),
            fontSize = 18.sp,
            fontFamily = Pretendard,
            fontWeight = SemiBold,
            color = Black18,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ActivatedDiscussionScreenPreview(
    @PreviewParameter(ActivatedDiscussionsUiStatePreviewParameterProvider::class)
    activatedDiscussions: ActivatedDiscussionsUiState,
) {
    ActivatedDiscussionScreen(
        onLoadMore = {},
        onClick = {},
        activatedDiscussions = activatedDiscussions,
    )
}
