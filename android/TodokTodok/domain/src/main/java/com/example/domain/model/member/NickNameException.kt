package com.example.domain.model.member

sealed class NickNameException : IllegalArgumentException() {
    object InvalidWhiteSpace : NickNameException()

    object InvalidCharacters : NickNameException()

    object InvalidLength : NickNameException()
}
