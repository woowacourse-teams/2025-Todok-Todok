package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.todoktodok.presentation.compose.discussion.my.MyDiscussionUiState

class MyDiscussionsUiStatePreviewParameterProvider : PreviewParameterProvider<MyDiscussionUiState> {
    override val values: Sequence<MyDiscussionUiState>
        get() =
            sequenceOf(
                MyDiscussionUiState(
                    CreatedDiscussionUiStatePreviewParameterProvider().values.first(),
                    ParticipatedDiscussionPreviewParameterProvider().values.first(),
                ),
            )
}
