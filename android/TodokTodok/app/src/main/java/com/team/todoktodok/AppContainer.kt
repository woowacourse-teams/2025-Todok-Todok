package com.team.todoktodok

import android.content.Context
import com.team.domain.ConnectivityObserver
import com.team.todoktodok.connectivity.NetworkConnectivityObserver
import com.team.todoktodok.data.datasource.token.TokenDataSource
import com.team.todoktodok.data.datasource.token.TokenLocalDataSource
import com.team.todoktodok.data.network.auth.AuthInterceptor
import com.team.todoktodok.data.network.auth.TokenAuthenticator
import com.team.todoktodok.data.network.auth.TokenRefreshDelegator
import com.team.todoktodok.di.DataSourceModule
import com.team.todoktodok.di.LocalDatabaseModule
import com.team.todoktodok.di.OkhttpModule
import com.team.todoktodok.di.RepositoryModule
import com.team.todoktodok.di.RetrofitModule
import com.team.todoktodok.di.ServiceModule

class AppContainer(
    context: Context,
) {
    val connectivityObserver: ConnectivityObserver by lazy {
        NetworkConnectivityObserver(context)
    }

    val tokenAuthenticator: TokenAuthenticator by lazy {
        TokenAuthenticator(
            tokenRefreshDelegate = { refreshTokenHandler },
        )
    }

    private val refreshTokenHandler: TokenRefreshDelegator by lazy {
        TokenRefreshDelegator(
            serviceModule.refreshService,
            tokenLocalDataSource,
        )
    }

    private val tokenLocalDataSource: TokenDataSource by lazy {
        TokenLocalDataSource(context)
    }

    val authInterceptor: AuthInterceptor by lazy {
        AuthInterceptor(tokenLocalDataSource)
    }

    private val retrofitModule: RetrofitModule by lazy {
        RetrofitModule(okHttpModule)
    }

    private val serviceModule: ServiceModule by lazy {
        ServiceModule(retrofitModule)
    }
    private val localDatabaseModule: LocalDatabaseModule by lazy {
        LocalDatabaseModule(context)
    }
    private val dataSourceModule: DataSourceModule by lazy {
        DataSourceModule(serviceModule, context)
    }

    private val okHttpModule: OkhttpModule by lazy {
        OkhttpModule(tokenAuthenticator, authInterceptor)
    }

    val repositoryModule: RepositoryModule by lazy {
        RepositoryModule(dataSourceModule)
    }
}
