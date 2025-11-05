package com.team.ui_xml.notification.navigation

import com.team.core.navigation.NotificationRoute
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
    abstract fun bindsNotificationRoute(route: NotificationNavigation): NotificationRoute
}
