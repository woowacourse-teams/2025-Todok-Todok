package com.team.domain.model.exception

sealed class ProfileImageExceptions(
    override val message: String,
) : TodokTodokExceptions() {
    data object EmptyContent : ProfileImageExceptions(
        "[ERROR] 파일이 비어있습니다",
    ) {
        private fun readResolve(): Any = EmptyContent
    }

    data object OverMaxSize : ProfileImageExceptions(
        "[ERROR] 파일 크기가 10MB 초과입니다",
    ) {
        private fun readResolve(): Any = OverMaxSize
    }

    data object NotImageFile : ProfileImageExceptions(
        "[ERROR] 이미지 파일이 아닙니다",
    ) {
        private fun readResolve(): Any = NotImageFile
    }
}
