package com.team.domain.model.exception

sealed class CommentExceptions(
    override val message: String,
) : TodokTodokExceptions() {
    data object EmptyContent :
        CommentExceptions("[ERROR] 댓글 내용을 입력해주세요")

    data object InvalidContentLength :
        CommentExceptions("[ERROR] 댓글 내용은 1자 이상, 2000자 이하여야 합니다")

    data object SelfReportNotAllowed :
        CommentExceptions("[ERROR] 자기 자신의 댓글은 신고할 수 없습니다")

    data object AlreadyReported :
        CommentExceptions("[ERROR] 이미 신고한 댓글입니다")

    data object NotBelongToDiscussion :
        CommentExceptions("[ERROR] 해당 토론방의 댓글이 아닙니다")

    data object CannotDeleteWithReplies :
        CommentExceptions("[ERROR] 대댓글이 존재하는 댓글은 삭제할 수 없습니다")

    data object OnlyOwnerCanModifyOrDelete :
        CommentExceptions("[ERROR] 자기 자신의 댓글만 수정/삭제 가능합니다")
}
