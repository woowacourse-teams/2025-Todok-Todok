package com.team.ui_xml.setting.navigation

import com.team.core.navigation.SettingRoute
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
    abstract fun bindsSettingRoute(route: SettingNavigation): SettingRoute
}
