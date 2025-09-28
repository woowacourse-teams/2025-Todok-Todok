package com.team.todoktodok.presentation.compose.discussion.all

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsScreen
import com.team.todoktodok.presentation.compose.discussion.latest.vm.LatestDiscussionViewModel
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionScreen
import com.team.todoktodok.presentation.compose.preview.AllDiscussionUiStatePreviewParameterProvider
import com.team.todoktodok.presentation.core.ExceptionMessageConverter

@Composable
fun AllDiscussionsScreen(
    latestDiscussionViewModel: LatestDiscussionViewModel,
    messageConverter: ExceptionMessageConverter,
    uiState: AllDiscussionsUiState,
    onClickDiscussion: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState.mode) {
        AllDiscussionMode.LATEST -> {
            LatestDiscussionsScreen(
                viewModel = latestDiscussionViewModel,
                messageConverter = messageConverter,
                onClick = { discussionId ->
                    onClickDiscussion(discussionId)
                },
                modifier = modifier,
            )
        }

        AllDiscussionMode.SEARCH -> {
            SearchDiscussionScreen(
                uiState = uiState.searchDiscussion,
                onClick = { discussionId ->
                    onClickDiscussion(discussionId)
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
}
