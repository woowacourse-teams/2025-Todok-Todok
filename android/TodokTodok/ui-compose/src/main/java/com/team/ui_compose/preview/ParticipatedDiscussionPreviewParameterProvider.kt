package com.team.ui_compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.ui_compose.my.participated.ParticipatedDiscussionsUiState

class ParticipatedDiscussionPreviewParameterProvider : PreviewParameterProvider<ParticipatedDiscussionsUiState> {
    override val values: Sequence<ParticipatedDiscussionsUiState>
        get() =
            sequenceOf(
                ParticipatedDiscussionsUiState(DiscussionUiStatePreviewParameterProvider().values.first()),
            )
}
