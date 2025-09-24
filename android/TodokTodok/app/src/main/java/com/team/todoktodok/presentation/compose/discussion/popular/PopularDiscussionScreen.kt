package com.team.todoktodok.presentation.compose.discussion.popular

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.component.DiscussionCard
import com.team.todoktodok.presentation.compose.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.preview.PopularDiscussionsPreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.Black18
import com.team.todoktodok.presentation.compose.theme.Pretendard

@Composable
fun PopularDiscussionsScreen(
    onClick: (Long) -> Unit,
    uiState: PopularDiscussionsUiState,
) {
    Column {
        PopularDiscussionsHeader()

        LazyRow(
            contentPadding = PaddingValues(5.dp),
        ) {
            items(items = uiState.discussions, key = { it.discussionId }) { item ->
                DiscussionCard(
                    uiState = item,
                    onClick = { onClick(item.discussionId) },
                    discussionCardType = DiscussionCardType.OpinionVisible,
                    modifier = Modifier.fillParentMaxWidth(0.9f),
                )
            }
        }
    }
}

@Composable
fun PopularDiscussionsHeader() {
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
fun PopularDiscussionsScreenPreview(
    @PreviewParameter(PopularDiscussionsPreviewParameterProvider::class)
    popularDiscussions: PopularDiscussionsUiState,
) {
    PopularDiscussionsScreen(
        onClick = {},
        uiState = popularDiscussions,
    )
}
