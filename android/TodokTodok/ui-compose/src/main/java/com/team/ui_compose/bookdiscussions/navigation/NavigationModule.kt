package com.team.ui_compose.bookdiscussions.navigation

import com.team.core.navigation.BookDiscussionsRoute
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
    abstract fun bindsBookDiscussionsRoute(route: BookDiscussionsNavigation): BookDiscussionsRoute
}
