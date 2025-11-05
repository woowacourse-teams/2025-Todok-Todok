package com.team.ui_compose.main.navigation

import com.team.core.navigation.MainRoute
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {
    @Binds
    @Singleton
    abstract fun bindsMainRoute(route: MainNavigation): MainRoute
}
