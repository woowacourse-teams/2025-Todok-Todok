package com.team.todoktodok.presentation.compose.my.participated

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiModel
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

data class ParticipatedDiscussionsUiState(
    val discussions: List<DiscussionUiModel> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.Default,
    val showMyDiscussion: Boolean = false,
    val memberId: Long = INITIALIZE_MEMBER_ID,
) {
    fun add(discussions: List<Discussion>): ParticipatedDiscussionsUiState {
        val newDiscussions =
            discussions
                .map { DiscussionUiModel(it) }
                .reversed()
        return copy(discussions = newDiscussions)
    }

    fun remove(discussionId: Long): ParticipatedDiscussionsUiState =
        copy(discussions = discussions.filter { it.discussionId != discussionId })

    fun modify(discussion: SerializationDiscussion): ParticipatedDiscussionsUiState =
        copy(
            discussions =
                discussions.map {
                    if (discussion.id == it.discussionId) {
                        DiscussionUiModel(
                            discussion.toDomain(),
                        )
                    } else {
                        it
                    }
                },
        )

    fun isEmpty(): Boolean = discussions.isEmpty()

    fun toggleShowMyDiscussion(isShow: Boolean): ParticipatedDiscussionsUiState = copy(showMyDiscussion = isShow)

    fun setMemberId(memberId: Long): ParticipatedDiscussionsUiState = copy(memberId = memberId)

    fun visibleDiscussions(showMyOnly: Boolean): List<DiscussionUiModel> =
        if (showMyOnly) {
            discussions.filter { it.writerId == memberId }
        } else {
            discussions
        }

    companion object {
        private const val INITIALIZE_MEMBER_ID = -1L
    }
}
