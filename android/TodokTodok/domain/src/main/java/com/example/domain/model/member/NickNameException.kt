package com.example.domain.model.member

sealed class NickNameException : Throwable() {
    data object InvalidWhiteSpace : NickNameException()
}
