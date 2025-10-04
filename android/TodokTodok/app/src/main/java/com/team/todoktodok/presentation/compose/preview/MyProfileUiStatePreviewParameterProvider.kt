package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.domain.model.member.Profile
import com.team.todoktodok.presentation.compose.my.model.MyProfileUiState

class MyProfileUiStatePreviewParameterProvider : PreviewParameterProvider<MyProfileUiState> {
    override val values: Sequence<MyProfileUiState>
        get() =
            sequenceOf(
                MyProfileUiState(
                    profile =
                        Profile(
                            memberId = 1,
                            nickname = "페토",
                            message = "안녕하세요",
                            profileImage = "",
                        ),
                    activatedBooks = emptyList(),
                    participatedDiscussions = emptyList(),
                    createdDiscussions = emptyList(),
                ),
            )
}
