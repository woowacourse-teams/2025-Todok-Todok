package com.team.ui_compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.ui_compose.component.DiscussionCardType
import com.team.ui_compose.discussion.activate.ActivatedDiscussionsUiState

class ActivatedDiscussionsUiStatePreviewParameterProvider : PreviewParameterProvider<ActivatedDiscussionsUiState> {
    override val values =
        sequenceOf(
            ActivatedDiscussionsUiState(
                discussions = DiscussionUiStatePreviewParameterProvider().values.first(),
                type = DiscussionCardType.Default,
            ),
        )
}
