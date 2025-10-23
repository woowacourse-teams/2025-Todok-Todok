package com.team.todoktodok.presentation.compose.my.liked

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiModel

data class LikedDiscussionsUiState(
    val discussions: List<DiscussionUiModel> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.Default,
) {
    fun setDiscussion(discussions: List<Discussion>): LikedDiscussionsUiState {
        val newDiscussions = discussions.map { DiscussionUiModel(it) }
        return copy(discussions = newDiscussions)
    }

    fun modifyMyDiscussionProfileImage(
        profileImage: String,
        memberId: Long,
    ): LikedDiscussionsUiState =
        copy(
            discussions =
                discussions.map { discussion ->
                    if (memberId == discussion.writerId) {
                        discussion.modifyWriterProfileImage(profileImage)
                    } else {
                        discussion
                    }
                },
        )

    fun isEmpty(): Boolean = discussions.isEmpty()
}
