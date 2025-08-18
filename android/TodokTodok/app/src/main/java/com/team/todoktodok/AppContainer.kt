package com.team.todoktodok

import android.content.Context
import com.team.todoktodok.data.datasource.token.TokenDataSource
import com.team.todoktodok.data.di.DataSourceModule
import com.team.todoktodok.data.di.OkhttpModule
import com.team.todoktodok.data.di.RepositoryModule
import com.team.todoktodok.data.di.RetrofitModule
import com.team.todoktodok.data.di.ServiceModule
import com.team.todoktodok.data.network.auth.AuthInterceptor

class  AppContainer(
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
        DataSourceModule(serviceModule, context)
    }

    val okHttpModule: OkhttpModule by lazy {
        OkhttpModule(authInterceptor)
    }

    val repositoryModule: RepositoryModule by lazy {
        RepositoryModule(dataSourceModule)
    }
}
