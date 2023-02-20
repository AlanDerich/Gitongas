package com.derich.gitongas.ui.data

import com.derich.gitongas.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMainActivity(): MainActivity = MainActivity.getInstance() as MainActivity

}