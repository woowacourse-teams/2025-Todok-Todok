package com.example.todoktodok

import com.example.todoktodok.data.di.DataSourceModule
import com.example.todoktodok.data.di.OkhttpModule
import com.example.todoktodok.data.di.RepositoryModule
import com.example.todoktodok.data.di.RetrofitModule
import com.example.todoktodok.data.di.ServiceModule

class AppContainer {
    private val dataSourceModule: DataSourceModule by lazy {
        DataSourceModule(serviceModule)
    }
    val repositoryModule: RepositoryModule by lazy {
        RepositoryModule(dataSourceModule)
    }

    val okHttpModule = OkhttpModule()

    val retrofitModule = RetrofitModule(okHttpModule)

    val serviceModule = ServiceModule(retrofitModule)
}
