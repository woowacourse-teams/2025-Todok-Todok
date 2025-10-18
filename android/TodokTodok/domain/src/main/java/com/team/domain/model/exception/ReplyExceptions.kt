package com.team.domain.model.exception

sealed class ReplyExceptions(
    override val message: String,
) : TodokTodokExceptions() {
    data object EmptyContent :
        ReplyExceptions("[ERROR] 대댓글 내용을 입력해주세요") {
        private fun readResolve(): Any = EmptyContent
    }

    data object InvalidContentLength :
        ReplyExceptions("[ERROR] 대댓글 내용은 1자 이상, 2000자 이하여야 합니다") {
        private fun readResolve(): Any = InvalidContentLength
    }

    data object SelfReportNotAllowed :
        ReplyExceptions("[ERROR] 자기 자신의 대댓글은 신고할 수 없습니다") {
        private fun readResolve(): Any = SelfReportNotAllowed
    }

    data object AlreadyReported :
        ReplyExceptions("[ERROR] 이미 신고한 대댓글입니다") {
        private fun readResolve(): Any = AlreadyReported
    }

    data object OnlyOwnerCanModifyOrDelete :
        ReplyExceptions("[ERROR] 자기 자신의 대댓글만 수정/삭제 가능합니다") {
        private fun readResolve(): Any = OnlyOwnerCanModifyOrDelete
    }

    data object CommentNotBelongToDiscussion :
        ReplyExceptions("[ERROR] 해당 토론방에 있는 댓글이 아닙니다") {
        private fun readResolve(): Any = CommentNotBelongToDiscussion
    }

    data object ReplyNotBelongToComment :
        ReplyExceptions("[ERROR] 해당 댓글에 있는 대댓글이 아닙니다") {
        private fun readResolve(): Any = ReplyNotBelongToComment
    }
}
