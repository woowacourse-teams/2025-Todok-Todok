package com.team.ui_compose.my.participated

import com.team.domain.model.Discussion
import com.team.ui_compose.component.DiscussionCardType
import com.team.ui_compose.discussion.model.DiscussionUiModel

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
        val myDiscussion = newDiscussions.filter { it.writerId == memberId }
        return copy(discussions = newDiscussions, myDiscussion = myDiscussion)
    }

    fun isEmpty(): Boolean = discussions.isEmpty()

    fun toggleShowMyDiscussion(isShow: Boolean): ParticipatedDiscussionsUiState = copy(showMyDiscussion = isShow)

    fun modifyMyDiscussionProfileImage(
        profileImage: String,
        myId: Long,
    ): ParticipatedDiscussionsUiState =
        copy(
            discussions =
                discussions.map { discussion ->
                    if (discussion.writerId == myId) {
                        discussion.modifyWriterProfileImage(profileImage)
                    } else {
                        discussion
                    }
                },
            myDiscussion =
                myDiscussion.map { discussion ->
                    discussion.modifyWriterProfileImage(profileImage)
                },
        )
}
