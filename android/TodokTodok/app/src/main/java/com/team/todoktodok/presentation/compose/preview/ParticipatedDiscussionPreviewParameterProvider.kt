package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.todoktodok.presentation.compose.my.participated.ParticipatedDiscussionsUiModel

class ParticipatedDiscussionPreviewParameterProvider : PreviewParameterProvider<ParticipatedDiscussionsUiModel> {
    override val values: Sequence<ParticipatedDiscussionsUiModel>
        get() =
            sequenceOf(
                ParticipatedDiscussionsUiModel(DiscussionUiStatePreviewParameterProvider().values.first()),
            )
}
