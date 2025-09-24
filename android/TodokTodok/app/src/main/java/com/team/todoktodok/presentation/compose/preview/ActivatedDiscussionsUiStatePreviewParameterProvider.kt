package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.presentation.compose.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.activate.ActivatedDiscussionsUiState
import com.team.todoktodok.presentation.xml.discussions.DiscussionUiState
import java.time.LocalDateTime

class ActivatedDiscussionsUiStatePreviewParameterProvider : PreviewParameterProvider<ActivatedDiscussionsUiState> {
    override val values =
        sequenceOf(
            ActivatedDiscussionsUiState(
                discussions =
                    (1L..5L).map { id ->
                        DiscussionUiState(
                            item =
                                Discussion(
                                    id = id,
                                    discussionTitle = "토론 주제 $id",
                                    book =
                                        Book(
                                            id = id,
                                            title = "책 제목 $id",
                                            author = "저자 $id",
                                            image = "",
                                        ),
                                    writer =
                                        User(
                                            id = id,
                                            nickname = Nickname("사용자$id"),
                                            profileImage = "",
                                        ),
                                    createAt = LocalDateTime.of(2025, 7, 12, 12, 0).plusDays(id),
                                    discussionOpinion = "이것은 토론 의견 $id 입니다.",
                                    likeCount = (id * 2).toInt(),
                                    commentCount = (id * 3).toInt(),
                                    isLikedByMe = id % 2 == 0L,
                                ),
                        )
                    },
                type = DiscussionCardType.Default,
            ),
        )
}
