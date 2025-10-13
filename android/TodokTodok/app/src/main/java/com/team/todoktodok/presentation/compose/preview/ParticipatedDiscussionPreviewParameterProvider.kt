package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.todoktodok.presentation.compose.my.participated.ParticipatedDiscussionsUiState

class ParticipatedDiscussionPreviewParameterProvider : PreviewParameterProvider<ParticipatedDiscussionsUiState> {
    override val values: Sequence<ParticipatedDiscussionsUiState>
        get() =
            sequenceOf(
                ParticipatedDiscussionsUiState(DiscussionUiStatePreviewParameterProvider().values.first()),
            )
}
