package com.team.domain.model.exception

sealed class BlockException(
    override val message: String,
) : TodokTodokExceptions() {
    data object AlreadyBlockedException : BlockException("[ERROR] 이미 차단한 회원입니다")
}
