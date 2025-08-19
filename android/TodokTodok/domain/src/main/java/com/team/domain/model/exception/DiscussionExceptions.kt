package com.team.domain.model.exception

sealed class DiscussionExceptions(
    override val message: String,
) : TodokTodokExceptions() {
    data object SelfReportNotAllowed :
        DiscussionExceptions("[ERROR] 자기 자신의 토론방은 신고할 수 없습니다")

    data object AlreadyReported :
        DiscussionExceptions("[ERROR] 이미 신고한 토론방입니다")

    data object CannotDeleteWithComments :
        DiscussionExceptions("[ERROR] 댓글이 존재하는 토론방은 삭제할 수 없습니다")

    data object OnlyOwnerCanModifyOrDelete :
        DiscussionExceptions("[ERROR] 자기 자신의 토론방만 수정/삭제 가능합니다")
}
