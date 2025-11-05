package com.team.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun subscribe(scope: CoroutineScope): Flow<Status>

    fun value(): Status

    enum class Status {
        Idle,
        Lost,
        Available,
    }
}
