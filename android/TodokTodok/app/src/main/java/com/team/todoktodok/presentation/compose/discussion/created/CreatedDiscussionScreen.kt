package com.team.todoktodok.presentation.compose.discussion.created

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.team.todoktodok.presentation.compose.component.DiscussionCard
import com.team.todoktodok.presentation.compose.preview.CreatedDiscussionUiStatePreviewParameterProvider

@Composable
fun CreatedDiscussionScreen(
    onClick: (Long) -> Unit,
    uiState: CreatedDiscussionsUiState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(vertical = 5.dp, horizontal = 10.dp)) {
        uiState.discussions.forEach { discussion ->
            DiscussionCard(
                discussion,
                onClick = { onClick(discussion.discussionId) },
                discussionCardType = uiState.type,
                modifier = Modifier.padding(top = 10.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreatedDiscussionScreenPreview(
    @PreviewParameter(CreatedDiscussionUiStatePreviewParameterProvider::class)
    createdDiscussions: CreatedDiscussionsUiState,
) {
    CreatedDiscussionScreen(
        onClick = {},
        uiState = createdDiscussions,
    )
}
