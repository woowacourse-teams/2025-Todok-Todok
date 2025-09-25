package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.todoktodok.presentation.compose.discussion.hot.HotDiscussionUiState

class HotDiscussionPreviewParameterProvider : PreviewParameterProvider<HotDiscussionUiState> {
    override val values: Sequence<HotDiscussionUiState> =
        sequenceOf(
            HotDiscussionUiState(
                popularDiscussions = PopularDiscussionsPreviewParameterProvider().values.first(),
                activatedDiscussions = ActivatedDiscussionsUiStatePreviewParameterProvider().values.first(),
            ),
        )
}
