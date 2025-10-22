package com.team.todoktodok.presentation.compose.my.participated

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiModel

data class ParticipatedDiscussionsUiState(
    val discussions: List<DiscussionUiModel> = emptyList(),
    val myDiscussion: List<DiscussionUiModel> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.Default,
    val showMyDiscussion: Boolean = false,
) {
    fun setDiscussion(
        discussions: List<Discussion>,
        memberId: Long,
    ): ParticipatedDiscussionsUiState {
        val newDiscussions =
            discussions
                .map { DiscussionUiModel(it) }
                .reversed()

        val myDiscussion = newDiscussions.filter { it.writerId == memberId }
        return copy(discussions = newDiscussions, myDiscussion = myDiscussion)
    }

    fun isEmpty(): Boolean = discussions.isEmpty()

    fun toggleShowMyDiscussion(isShow: Boolean): ParticipatedDiscussionsUiState = copy(showMyDiscussion = isShow)

    fun modifyMyDiscussionProfileImage(
        profileImage: String,
        memberId: Long,
    ): ParticipatedDiscussionsUiState =
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
}
