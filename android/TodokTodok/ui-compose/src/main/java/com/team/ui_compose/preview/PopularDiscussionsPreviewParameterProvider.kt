package com.team.ui_compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.ui_compose.discussion.popular.PopularDiscussionsUiState

class PopularDiscussionsPreviewParameterProvider : PreviewParameterProvider<PopularDiscussionsUiState> {
    override val values: Sequence<PopularDiscussionsUiState> =
        sequenceOf(
            PopularDiscussionsUiState(
                discussions = DiscussionUiStatePreviewParameterProvider().values.first(),
            ),
        )
}
