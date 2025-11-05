package com.team.data.di

import com.team.data.BuildConfig
import com.team.data.datasource.token.TokenLocalDataSource
import com.team.data.log.PrettyJsonLogger
import com.team.data.network.auth.AuthInterceptor
import com.team.data.network.auth.TokenAuthenticator
import com.team.data.network.service.RefreshService
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
    @Singleton
    fun provideTokenAuthenticator(
        service: RefreshService,
        tokenDataSource: TokenLocalDataSource,
    ): TokenAuthenticator = TokenAuthenticator(service, tokenDataSource)

    @Auth
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

    @Client
    @Provides
    @Singleton
    fun provideClient(logger: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(logger)
            .build()
}
