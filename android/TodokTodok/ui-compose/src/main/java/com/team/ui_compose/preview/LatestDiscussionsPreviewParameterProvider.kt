package com.team.ui_compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.ui_compose.component.DiscussionCardType
import com.team.ui_compose.discussion.latest.LatestDiscussionsUiState

class LatestDiscussionsPreviewParameterProvider : PreviewParameterProvider<LatestDiscussionsUiState> {
    override val values: Sequence<LatestDiscussionsUiState>
        get() =
            sequenceOf(
                LatestDiscussionsUiState(
                    discussions = DiscussionUiStatePreviewParameterProvider().values.first(),
                    type = DiscussionCardType.Default,
                ),
                LatestDiscussionsUiState(
                    discussions = DiscussionUiStatePreviewParameterProvider().values.first().take(2),
                    type = DiscussionCardType.Default,
                ),
            )
}
