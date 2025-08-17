package com.team.todoktodok.data.core.ext

import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.todoktodok.data.network.auth.AuthInterceptor.Companion.AUTHORIZATION_NAME
import retrofit2.Response

suspend fun <R, T> Response<T>.extractAccessToken(onTokenReceived: suspend (token: String) -> R): NetworkResult<R> {
    if (!isSuccessful) {
        return NetworkResult.Failure(TodokTodokExceptions.from(code(), message()))
    }

    val accessToken =
        headers()[AUTHORIZATION_NAME]
            ?: return NetworkResult.Failure(TodokTodokExceptions.MissingLocationHeaderException)

    return runCatching {
        NetworkResult.Success(onTokenReceived(accessToken))
    }.getOrElse {
        NetworkResult.Failure(TodokTodokExceptions.UnknownException)
    }
}
