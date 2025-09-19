package com.team.domain

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun subscribe(): Flow<Status>

    fun value(): Status

    enum class Status {
        Idle,
        Lost,
        Available,
    }
}
