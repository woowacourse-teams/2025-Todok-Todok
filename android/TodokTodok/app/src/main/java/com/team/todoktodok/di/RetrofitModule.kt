package com.team.todoktodok.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.team.todoktodok.BuildConfig
import com.team.todoktodok.data.network.adapter.TodokTodokCallAdapterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
class RetrofitModule(
    okHttpClient: OkhttpModule,
) {
    private val baseUrl = BuildConfig.BASE_URL
    private val json = Json { ignoreUnknownKeys = true }
    private val contentType = "application/json".toMediaType()

    private val defaultRetrofit: Retrofit.Builder =
        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(TodokTodokCallAdapterFactory())
            .addConverterFactory(json.asConverterFactory(contentType))

    val authRetrofit: Retrofit =
        defaultRetrofit
            .client(okHttpClient.authClient)
            .build()

    val retrofit: Retrofit =
        defaultRetrofit
            .client(okHttpClient.client)
            .build()

    fun <T> createAuthService(serviceClass: Class<T>): T = authRetrofit.create(serviceClass)

    fun <T> createService(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}
