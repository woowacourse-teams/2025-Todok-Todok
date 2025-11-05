package com.team.ui_compose.main

import kotlinx.serialization.Serializable

sealed interface MainRoute {
    @Serializable
    data object Discussion : MainRoute

    @Serializable
    data object My : MainRoute
}
