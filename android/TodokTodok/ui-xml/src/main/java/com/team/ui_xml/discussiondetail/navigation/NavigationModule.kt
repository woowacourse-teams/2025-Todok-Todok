package com.team.ui_xml.discussiondetail.navigation

import com.team.core.navigation.DiscussionDetailRoute
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
    abstract fun bindsDiscussionDetailRoute(route: DiscussionDetailNavigation): DiscussionDetailRoute
}
