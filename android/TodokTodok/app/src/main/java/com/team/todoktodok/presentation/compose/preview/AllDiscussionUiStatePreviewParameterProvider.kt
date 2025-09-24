package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.todoktodok.presentation.compose.discussion.all.AllDiscussionMode
import com.team.todoktodok.presentation.compose.discussion.all.AllDiscussionsUiState

class AllDiscussionUiStatePreviewParameterProvider : PreviewParameterProvider<AllDiscussionsUiState> {
    override val values: Sequence<AllDiscussionsUiState>
        get() =
            sequenceOf(
                AllDiscussionsUiState(
                    mode = AllDiscussionMode.LATEST,
                    latestDiscussion = LatestDiscussionsPreviewParameterProvider().values.first(),
                ),
                AllDiscussionsUiState(
                    mode = AllDiscussionMode.SEARCH,
                    searchDiscussion = SearchDiscussionsUiStatePreviewParameterProvider().values.first(),
                ),
            )
}
