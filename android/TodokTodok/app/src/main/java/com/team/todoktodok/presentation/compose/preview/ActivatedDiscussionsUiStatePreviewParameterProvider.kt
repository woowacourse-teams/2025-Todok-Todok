package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.activate.ActivatedDiscussionsUiState

class ActivatedDiscussionsUiStatePreviewParameterProvider : PreviewParameterProvider<ActivatedDiscussionsUiState> {
    override val values =
        sequenceOf(
            ActivatedDiscussionsUiState(
                discussions = DiscussionUiStatePreviewParameterProvider().values.first(),
                type = DiscussionCardType.Default,
            ),
        )
}
