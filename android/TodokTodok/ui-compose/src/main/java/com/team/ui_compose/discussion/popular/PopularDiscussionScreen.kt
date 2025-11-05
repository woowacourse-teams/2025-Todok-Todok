package com.team.ui_compose.discussion.popular

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
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
import com.team.ui_compose.R
import com.team.ui_compose.component.DiscussionCard
import com.team.ui_compose.component.DiscussionCardType
import com.team.ui_compose.preview.PopularDiscussionsPreviewParameterProvider
import com.team.ui_compose.theme.Black18
import com.team.ui_compose.theme.Pretendard

@Composable
fun PopularDiscussionsScreen(
    uiState: PopularDiscussionsUiState,
    onClickDiscussion: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        PopularDiscussionsHeader()
        Spacer(Modifier.height(5.dp))

        LazyRow(
            contentPadding = PaddingValues(5.dp),
        ) {
            items(items = uiState.discussions, key = { it.discussionId }) { item ->
                DiscussionCard(
                    uiState = item,
                    discussionCardType = DiscussionCardType.OpinionVisible,
                    onClick = { onClickDiscussion(it) },
                    modifier = Modifier.Companion.fillParentMaxWidth(0.9f),
                )
            }
        }
    }
}

@Composable
private fun PopularDiscussionsHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.img_fire),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )

        Text(
            text = stringResource(R.string.discussions_hot_discussion_rooms),
            fontSize = 18.sp,
            fontFamily = Pretendard,
            fontWeight = SemiBold,
            color = Black18,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PopularDiscussionsScreenPreview(
    @PreviewParameter(PopularDiscussionsPreviewParameterProvider::class)
    popularDiscussions: PopularDiscussionsUiState,
) {
    PopularDiscussionsScreen(
        uiState = popularDiscussions,
        onClickDiscussion = {},
    )
}
