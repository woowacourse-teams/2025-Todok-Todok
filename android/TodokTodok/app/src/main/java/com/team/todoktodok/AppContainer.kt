package com.team.todoktodok

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.team.todoktodok.data.datasource.token.TokenDataSource
import com.team.todoktodok.data.datasource.token.TokenLocalDataSource
import com.team.todoktodok.data.di.DataSourceModule
import com.team.todoktodok.data.di.LocalDatabaseModule
import com.team.todoktodok.data.di.OkhttpModule
import com.team.todoktodok.data.di.RepositoryModule
import com.team.todoktodok.data.di.RetrofitModule
import com.team.todoktodok.data.di.ServiceModule
import com.team.todoktodok.data.network.auth.AuthInterceptor
import com.team.todoktodok.data.network.auth.TokenAuthenticator
import com.team.todoktodok.data.network.auth.TokenRefreshDelegate
import com.team.todoktodok.data.network.auth.TokenRefreshDelegator

class AppContainer(
    context: Context,
) {
    val tokenAuthenticator: TokenAuthenticator by lazy {
        TokenAuthenticator(
            tokenRefreshDelegate = { refreshTokenHandler },
            scope = ProcessLifecycleOwner.get().lifecycleScope,
        )
    }

    private val refreshTokenHandler: TokenRefreshDelegate by lazy {
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
        DataSourceModule(serviceModule, localDatabaseModule, context)
    }

    private val okHttpModule: OkhttpModule by lazy {
        OkhttpModule(tokenAuthenticator, authInterceptor)
    }

    val repositoryModule: RepositoryModule by lazy {
        RepositoryModule(dataSourceModule)
    }
}
