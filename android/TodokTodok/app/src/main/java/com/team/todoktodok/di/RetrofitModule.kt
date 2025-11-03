package com.team.todoktodok.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.team.todoktodok.BuildConfig
import com.team.todoktodok.data.network.adapter.TodokTodokCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private val json =
        Json {
            ignoreUnknownKeys = true
        }

    private val builder: Retrofit.Builder =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(TodokTodokCallAdapterFactory())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))

    @Provides
    @Singleton
    fun provideAuthRetrofit(
        @Auth okHttpClient: OkHttpClient,
    ): Retrofit =
        builder
            .client(okHttpClient)
            .build()

    @Provides
    @Client
    @Singleton
    fun provideRetrofit(
        @Client okHttpClient: OkHttpClient,
    ): Retrofit =
        builder
            .client(okHttpClient)
            .build()
}
