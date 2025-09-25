package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.todoktodok.presentation.compose.discussion.created.CreatedDiscussionsUiState

class CreatedDiscussionUiStatePreviewParameterProvider : PreviewParameterProvider<CreatedDiscussionsUiState> {
    override val values: Sequence<CreatedDiscussionsUiState>
        get() =
            sequenceOf(
                CreatedDiscussionsUiState(
                    discussions = DiscussionUiStatePreviewParameterProvider().values.first(),
                ),
            )
}
