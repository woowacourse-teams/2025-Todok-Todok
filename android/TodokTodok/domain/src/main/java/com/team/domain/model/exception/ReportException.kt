package com.team.domain.model.exception

sealed class ReportException(
    override val message: String,
) : TodokTodokExceptions() {
    data object AlreadyReportedException : ReportException("[ERROR] 이미 신고한 회원입니다") {
        private fun readResolve(): Any = AlreadyReportedException
    }
}
