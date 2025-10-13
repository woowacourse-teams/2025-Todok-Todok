package com.team.domain.model.exception

sealed class ISBNException(
    override val message: String,
) : TodokTodokExceptions() {
    data object InvalidLength : ISBNException("[ERROR] ISBN은 13자리 입니다") {
        private fun readResolve(): Any = InvalidLength
    }

    data object InvalidFormat : ISBNException("[ERROR] ISBN 형식에는 숫자만 들어올 수 있습니다") {
        private fun readResolve(): Any = InvalidFormat
    }
}
