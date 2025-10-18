package com.team.todoktodok.data.network.auth

fun interface TokenRefreshDelegate {
    suspend fun refresh(): String?
}
