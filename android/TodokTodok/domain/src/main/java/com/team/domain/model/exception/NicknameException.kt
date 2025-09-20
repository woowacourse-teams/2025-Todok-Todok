package com.team.domain.model.exception

sealed class NicknameException(
    override val message: String,
) : TodokTodokExceptions() {
    data object DuplicateNicknameException : NicknameException("[ERROR] 이미 존재하는 닉네임입니다") {
        private fun readResolve(): Any = DuplicateNicknameException
    }

    data object InvalidNicknameLengthException :
        NicknameException("[ERROR] 닉네임은 1자 이상, 8자 이하여야 합니다") {
        private fun readResolve(): Any = InvalidNicknameLengthException
    }

    data object EmptyNicknameLengthException : NicknameException("[ERROR] 닉네임을 입력해주세요") {
        private fun readResolve(): Any = EmptyNicknameLengthException
    }
}
