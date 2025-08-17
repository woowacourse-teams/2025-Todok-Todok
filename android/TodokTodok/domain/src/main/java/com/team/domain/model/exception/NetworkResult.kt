package com.team.domain.model.exception

sealed class NetworkResult<out T> {
    data class Success<T>(
        val data: T,
    ) : NetworkResult<T>()

    data class Failure(
        val exception: TokdokTodokExceptions,
    ) : NetworkResult<Nothing>()
}

fun <T> NetworkResult<T>.onSuccess(action: (T) -> Unit): NetworkResult<T> =
    when (this) {
        is NetworkResult.Success<T> -> {
            action(data)
            this
        }

        is NetworkResult.Failure -> {
            this
        }
    }

fun <T> NetworkResult<T>.onFailure(action: (TokdokTodokExceptions) -> Unit): NetworkResult<T> =
    when (this) {
        is NetworkResult.Success<T> -> {
            this
        }

        is NetworkResult.Failure -> {
            action(exception)
            this
        }
    }

fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> =
    when (this) {
        is NetworkResult.Success -> NetworkResult.Success(transform(data))
        is NetworkResult.Failure -> this
    }
