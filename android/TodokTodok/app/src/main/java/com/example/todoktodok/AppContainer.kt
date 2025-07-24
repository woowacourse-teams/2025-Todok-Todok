package com.example.todoktodok

import android.content.Context
import com.example.todoktodok.data.datasource.token.TokenDataSource
import com.example.todoktodok.data.di.DataSourceModule
import com.example.todoktodok.data.di.OkhttpModule
import com.example.todoktodok.data.di.RepositoryModule
import com.example.todoktodok.data.di.RetrofitModule
import com.example.todoktodok.data.di.ServiceModule
import com.example.todoktodok.data.network.auth.AuthInterceptor

class AppContainer(
    context: Context,
) {
    private val tokenDataSource: TokenDataSource by lazy {
        TokenDataSource(context)
    }

    val authInterceptor: AuthInterceptor by lazy {
        AuthInterceptor(tokenDataSource)
    }

    val retrofitModule: RetrofitModule by lazy {
        RetrofitModule(okHttpModule)
    }

    val serviceModule: ServiceModule by lazy {
        ServiceModule(retrofitModule)
    }

    val dataSourceModule: DataSourceModule by lazy {
        DataSourceModule(serviceModule)
    }

    val okHttpModule: OkhttpModule by lazy {
        OkhttpModule(authInterceptor)
    }

    val repositoryModule: RepositoryModule by lazy {
        RepositoryModule(dataSourceModule)
    }
}
