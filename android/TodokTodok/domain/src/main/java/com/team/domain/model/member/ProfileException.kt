package com.team.domain.model.member

sealed class ProfileException : Throwable() {
    data object SameMessageModification : ProfileException()
}
