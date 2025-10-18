package com.team.domain.model.exception

sealed class BookImageException(
    override val message: String,
) : TodokTodokExceptions() {
    data object InvalidUrl : BookImageException("[ERROR] 이미지 URL이 올바르지 않습니다") {
        private fun readResolve(): Any = InvalidUrl
    }
}
