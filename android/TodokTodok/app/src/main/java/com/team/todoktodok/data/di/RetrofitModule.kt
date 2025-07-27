package com.team.todoktodok.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.team.todoktodok.BuildConfig
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RetrofitModule(
    okHttpClient: OkhttpModule,
) {
    private val baseUrl = BuildConfig.BASE_URL
    private val json = Json { ignoreUnknownKeys = true }

    private val contentType = "application/json".toMediaType()

    @OptIn(ExperimentalSerializationApi::class)
    val instance: Retrofit =
        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient.instance)
            .addConverterFactory(
                json.asConverterFactory(contentType),
            ).build()
}
