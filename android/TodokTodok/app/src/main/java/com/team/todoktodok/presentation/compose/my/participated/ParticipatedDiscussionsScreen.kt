package com.team.todoktodok.presentation.compose.my.participated

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.team.todoktodok.presentation.compose.core.component.DiscussionCard
import com.team.todoktodok.presentation.compose.preview.ParticipatedDiscussionPreviewParameterProvider
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity

@Composable
fun ParticipatedDiscussionsScreen(
    uiState: ParticipatedDiscussionsUiState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(modifier = modifier.padding(10.dp)) {
        uiState.discussions.forEach { discussion ->
            DiscussionCard(
                uiState = discussion,
                discussionCardType = uiState.type,
                modifier = Modifier.padding(top = 10.dp),
                onClick = {
                    context
                        .startActivity(
                            DiscussionDetailActivity.Intent(
                                context,
                                discussion.discussionId,
                            ),
                        )
                },
            )
        }
    }
}

@Preview
@Composable
private fun ParticipatedDiscussionsScreenPreview(
    @PreviewParameter(ParticipatedDiscussionPreviewParameterProvider::class)
    uiState: ParticipatedDiscussionsUiState,
) {
    ParticipatedDiscussionsScreen(uiState = uiState)
}
