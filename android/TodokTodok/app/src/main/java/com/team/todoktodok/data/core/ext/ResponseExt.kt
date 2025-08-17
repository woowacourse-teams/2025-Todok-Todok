package com.team.todoktodok.data.core.ext

import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TokdokTodokExceptions
import com.team.todoktodok.data.network.auth.AuthInterceptor.Companion.AUTHORIZATION_NAME
import retrofit2.Response

suspend fun <R, T> Response<T>.extractAccessToken(onTokenReceived: suspend (token: String) -> R): NetworkResult<R> {
    if (!isSuccessful) {
        return NetworkResult.Failure(TokdokTodokExceptions.from(code(), message()))
    }

    val accessToken =
        headers()[AUTHORIZATION_NAME]
            ?: return NetworkResult.Failure(TokdokTodokExceptions.MissingLocationHeaderException)

    return NetworkResult.Success(onTokenReceived(accessToken))
}
