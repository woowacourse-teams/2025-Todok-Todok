package com.team.domain.model.member

sealed class NickNameException : IllegalArgumentException() {
    data object InvalidWhiteSpace : NickNameException()

    data object InvalidCharacters : NickNameException()

    data object InvalidLength : NickNameException()

    data object SameNicknameModification : NickNameException()
}
