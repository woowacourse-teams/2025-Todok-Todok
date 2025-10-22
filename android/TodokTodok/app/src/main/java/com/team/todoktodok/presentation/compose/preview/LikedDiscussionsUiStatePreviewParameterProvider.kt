package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.my.liked.LikedDiscussionsUiState

class LikedDiscussionsUiStatePreviewParameterProvider : PreviewParameterProvider<LikedDiscussionsUiState> {
    override val values =
        sequenceOf(
            LikedDiscussionsUiState(
                discussions = DiscussionUiStatePreviewParameterProvider().values.first(),
                type = DiscussionCardType.Default,
            ),
        )
}
