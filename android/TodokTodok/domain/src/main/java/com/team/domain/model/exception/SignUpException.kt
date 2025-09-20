package com.team.domain.model.exception

sealed class SignUpException(
    override val message: String,
) : TodokTodokExceptions() {
    data object DuplicateEmailException : SignUpException("[ERROR] 이미 가입된 이메일입니다") {
        private fun readResolve(): Any = DuplicateEmailException
    }

    data object InvalidTokenException : SignUpException("[ERROR] 소셜 로그인을 하지 않은 이메일입니다") {
        private fun readResolve(): Any = InvalidTokenException
    }

    data object InvalidFormatEmailException : SignUpException("[ERROR] 올바른 이메일 형식을 입력해주세요") {
        private fun readResolve(): Any = InvalidFormatEmailException
    }

    data object ProfileImageNotExistException : SignUpException("[ERROR] 프로필 이미지를 입력해주세요") {
        private fun readResolve(): Any = ProfileImageNotExistException
    }
}
