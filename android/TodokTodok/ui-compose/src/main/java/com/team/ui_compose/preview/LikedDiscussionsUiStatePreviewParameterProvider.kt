package com.team.ui_compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.ui_compose.component.DiscussionCardType

class LikedDiscussionsUiStatePreviewParameterProvider : PreviewParameterProvider<com.team.ui_compose.my.liked.LikedDiscussionsUiState> {
    override val values =
        sequenceOf(
            _root_ide_package_.com.team.ui_compose.my.liked.LikedDiscussionsUiState(
                discussions = DiscussionUiStatePreviewParameterProvider().values.first(),
                type = DiscussionCardType.Default,
            ),
        )
}
