package com.team.domain.model.exception

sealed class NicknameException(
    override val message: String,
) : TodokTodokExceptions() {
    data object DuplicateNickname : NicknameException("[ERROR] 이미 존재하는 닉네임입니다") {
        private fun readResolve(): Any = DuplicateNickname
    }

    data object InvalidNicknameLength :
        NicknameException("[ERROR] 닉네임은 1자 이상, 8자 이하여야 합니다") {
        private fun readResolve(): Any = InvalidNicknameLength
    }

    data object EmptyNicknameLength : NicknameException("[ERROR] 닉네임을 입력해주세요") {
        private fun readResolve(): Any = EmptyNicknameLength
    }
}
