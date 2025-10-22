package com.team.todoktodok.presentation.compose.main

import kotlinx.serialization.Serializable

@Serializable
sealed class MainDestination {
    @Serializable
    data object Discussion : MainDestination()

    @Serializable
    data object My : MainDestination()
}
