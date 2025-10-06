package com.team.domain.model.exception

sealed class SignUpException(
    override val message: String,
) : TodokTodokExceptions() {
    data object DuplicateEmail : SignUpException("[ERROR] 이미 가입된 이메일입니다") {
        private fun readResolve(): Any = DuplicateEmail
    }

    data object InvalidToken : SignUpException("[ERROR] 소셜 로그인을 하지 않은 이메일입니다") {
        private fun readResolve(): Any = InvalidToken
    }

    data object InvalidFormatEmail : SignUpException("[ERROR] 올바른 이메일 형식을 입력해주세요") {
        private fun readResolve(): Any = InvalidFormatEmail
    }

    data object ProfileImageNotExist : SignUpException("[ERROR] 프로필 이미지를 입력해주세요") {
        private fun readResolve(): Any = ProfileImageNotExist
    }
}
