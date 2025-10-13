package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiModel
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionsUiState
import java.time.LocalDateTime

class SearchDiscussionsUiStatePreviewParameterProvider : PreviewParameterProvider<SearchDiscussionsUiState> {
    override val values: Sequence<SearchDiscussionsUiState>
        get() {
            val dummyDiscussion1 =
                DiscussionUiModel(
                    Discussion(
                        id = 1L,
                        book =
                            Book(
                                id = 101L,
                                title = "클린 코드",
                                author = "로버트 C. 마틴",
                                image = "https://dummyimage.com/200x300/cccccc/000000.png&text=Clean+Code",
                            ),
                        discussionTitle = "클린 코드에 대한 생각",
                        discussionOpinion = "가독성이 정말 중요하다는 걸 느꼈습니다.",
                        writer =
                            User(
                                id = 11L,
                                nickname = Nickname("개발자A"),
                                profileImage = "https://dummyimage.com/100x100/aaaaaa/000000.png&text=A",
                            ),
                        createAt = LocalDateTime.now().minusDays(1),
                        likeCount = 12,
                        commentCount = 3,
                        viewCount = 0,
                        isLikedByMe = true,
                    ),
                )

            val dummyDiscussion2 =
                DiscussionUiModel(
                    Discussion(
                        id = 2L,
                        book =
                            Book(
                                id = 102L,
                                title = "리팩터링",
                                author = "마틴 파울러",
                                image = "https://dummyimage.com/200x300/cccccc/000000.png&text=Refactoring",
                            ),
                        discussionTitle = "리팩터링 경험 공유",
                        discussionOpinion = "리팩터링 후 코드 유지보수가 훨씬 쉬워졌습니다.",
                        writer =
                            User(
                                id = 12L,
                                nickname = Nickname("개발자B"),
                                profileImage = "https://dummyimage.com/100x100/bbbbbb/000000.png&text=B",
                            ),
                        createAt = LocalDateTime.now().minusHours(5),
                        likeCount = 7,
                        commentCount = 1,
                        viewCount = 0,
                        isLikedByMe = false,
                    ),
                )

            return sequenceOf(
                SearchDiscussionsUiState(
                    discussions = listOf(dummyDiscussion1, dummyDiscussion2),
                    type = DiscussionCardType.QueryHighlighting("코드"),
                ),
            )
        }
}
