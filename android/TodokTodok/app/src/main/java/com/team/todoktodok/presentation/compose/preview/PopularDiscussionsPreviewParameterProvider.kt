package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.todoktodok.presentation.compose.discussion.popular.PopularDiscussionsUiState

class PopularDiscussionsPreviewParameterProvider : PreviewParameterProvider<PopularDiscussionsUiState> {
    override val values: Sequence<PopularDiscussionsUiState> =
        sequenceOf(
            PopularDiscussionsUiState(
                discussions = DiscussionUiStatePreviewParameterProvider().values.first(),
            ),
        )
}
