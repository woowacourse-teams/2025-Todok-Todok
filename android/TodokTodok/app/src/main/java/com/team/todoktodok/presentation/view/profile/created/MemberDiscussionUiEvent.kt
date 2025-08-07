package com.team.todoktodok.presentation.view.profile.created

sealed interface MemberDiscussionUiEvent {
    data class NavigateToDetail(
        val discussionId: Long,
    ) : MemberDiscussionUiEvent
}
