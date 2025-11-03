package com.team.todoktodok.di

import com.team.todoktodok.BuildConfig
import com.team.todoktodok.data.datasource.token.TokenDataSource
import com.team.todoktodok.data.network.auth.AuthInterceptor
import com.team.todoktodok.data.network.auth.TokenAuthenticator2
import com.team.todoktodok.data.network.service.RefreshService
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
    @Singleton
    fun provideTokenAuthenticator(
        service: RefreshService,
        tokenDataSource: TokenDataSource,
    ): TokenAuthenticator2 = TokenAuthenticator2(service, tokenDataSource)

    @Auth
    @Provides
    @Singleton
    fun provideAuthClient(
        tokenAuthenticator: TokenAuthenticator2,
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
