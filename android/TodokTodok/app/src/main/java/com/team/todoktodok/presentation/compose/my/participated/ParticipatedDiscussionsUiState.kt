package com.team.todoktodok.presentation.compose.my.participated

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState

data class ParticipatedDiscussionsUiState(
    val discussions: List<DiscussionUiState> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.Default,
    val showMyDiscussion: Boolean = false,
    val memberId: Long = INITIALIZE_MEMBER_ID,
) {
    fun add(discussions: List<Discussion>): ParticipatedDiscussionsUiState {
        val newDiscussions =
            discussions
                .map { DiscussionUiState(it) }
                .reversed()
        return copy(discussions = newDiscussions)
    }

    fun remove(discussionId: Long): ParticipatedDiscussionsUiState =
        copy(discussions = discussions.filter { it.discussionId != discussionId })

    fun isEmpty(): Boolean = discussions.isEmpty()

    fun toggleShowMyDiscussion(isShow: Boolean): ParticipatedDiscussionsUiState = copy(showMyDiscussion = isShow)

    fun setMemberId(memberId: Long): ParticipatedDiscussionsUiState = copy(memberId = memberId)

    fun visibleDiscussions(showMyOnly: Boolean): List<DiscussionUiState> =
        if (showMyOnly) {
            discussions.filter { it.writerId == memberId }
        } else {
            discussions
        }

    companion object {
        private const val INITIALIZE_MEMBER_ID = -1L
    }
}
