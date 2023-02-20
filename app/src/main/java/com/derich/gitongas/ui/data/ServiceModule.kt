package com.derich.gitongas.ui.data

import com.derich.gitongas.ui.common.LogService
import com.derich.gitongas.ui.common.LogServiceImpl
import com.derich.gitongas.ui.common.firestorequeries.StorageService
import com.derich.gitongas.ui.common.firestorequeries.StorageServiceImpl
import com.derich.gitongas.ui.screens.login.AuthService
import com.derich.gitongas.ui.screens.login.AuthServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun bindAccountService(
        accountServiceImpl: AuthServiceImpl
    ): AuthService

    @Binds abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds abstract fun provideLogService(impl: LogServiceImpl): LogService
}