package com.team.todoktodok.data.di

import com.team.todoktodok.BuildConfig
import com.team.todoktodok.data.network.auth.AuthInterceptor
import com.team.todoktodok.data.network.auth.TokenAuthenticator
import com.team.todoktodok.data.network.auth.TokenRefreshDelegator
import com.team.todoktodok.log.PrettyJsonLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkhttpModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(PrettyJsonLogger()).apply {
            if (BuildConfig.DEBUG) {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                setLevel(HttpLoggingInterceptor.Level.NONE)
            }
        }

    @Provides
    fun provideTokenAuthenticator(tokenRefreshDelegator: TokenRefreshDelegator): TokenAuthenticator =
        TokenAuthenticator(
            tokenRefreshDelegate = { tokenRefreshDelegator },
        )

    @Provides
    @Singleton
    fun provideAuthClient(
        tokenAuthenticator: TokenAuthenticator,
        authInterceptor: AuthInterceptor,
        logger: HttpLoggingInterceptor,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .authenticator(tokenAuthenticator)
            .addInterceptor(authInterceptor)
            .addInterceptor(logger)
            .build()

    @Provides
    @Singleton
    fun provideClient(logger: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(logger)
            .build()
}
