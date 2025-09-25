package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsUiState

class LatestDiscussionsPreviewParameterProvider : PreviewParameterProvider<LatestDiscussionsUiState> {
    override val values: Sequence<LatestDiscussionsUiState>
        get() =
            sequenceOf(
                LatestDiscussionsUiState(
                    discussions = DiscussionUiStatePreviewParameterProvider().values.first(),
                    type = DiscussionCardType.Default,
                ),
            )
}
