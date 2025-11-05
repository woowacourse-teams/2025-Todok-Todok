package com.team.ui_compose.ext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout

suspend fun <T> Flow<T>.getOrAwaitValue(timeoutMillis: Long = 2000): T =
    withTimeout(timeoutMillis) {
        this@getOrAwaitValue.first()
    }
