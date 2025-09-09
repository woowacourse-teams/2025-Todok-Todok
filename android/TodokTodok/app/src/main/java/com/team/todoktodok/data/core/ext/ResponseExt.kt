package com.team.todoktodok.data.core.ext

import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.todoktodok.data.network.auth.AuthInterceptor.Companion.AUTHORIZATION_NAME
import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.response.LoginResponse
import retrofit2.Response
import java.net.HttpURLConnection.HTTP_CREATED
import java.net.HttpURLConnection.HTTP_NO_CONTENT

suspend fun <R> Response<LoginResponse>.extractTokens(
    onTokenReceived: suspend (accessToken: String, refreshToken: String) -> R,
): NetworkResult<R> {
    if (!isSuccessful) {
        return NetworkResult.Failure(TodokTodokExceptions.from(code(), message()))
    }

    val accessToken =
        headers()[AUTHORIZATION_NAME]
            ?: return NetworkResult.Failure(TodokTodokExceptions.MissingLocationHeaderException)

    val refreshToken = body()?.refreshToken

    if (refreshToken.isNullOrBlank()) {
        return NetworkResult.Failure(TodokTodokExceptions.RefreshTokenNotReceivedException)
    }
    return runCatching {
        NetworkResult.Success(onTokenReceived(accessToken, refreshToken))
    }.getOrElse {
        NetworkResult.Failure(TodokTodokExceptions.UnknownException(it))
    }
}

fun <T> Response<T>.mapToggleLikeResponse(): NetworkResult<LikeAction> =
    if (isSuccessful) {
        when (code()) {
            HTTP_CREATED -> NetworkResult.Success(LikeAction.LIKE)
            HTTP_NO_CONTENT -> NetworkResult.Success(LikeAction.UNLIKE)
            else -> NetworkResult.Failure(TodokTodokExceptions.UnknownException(null))
        }
    } else {
        val msg = errorBody()?.string()
        NetworkResult.Failure(TodokTodokExceptions.from(code(), msg))
    }
