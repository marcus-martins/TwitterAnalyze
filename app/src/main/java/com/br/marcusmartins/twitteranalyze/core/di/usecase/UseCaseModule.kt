package com.br.marcusmartins.twitteranalyze.core.di.usecase

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.*
import javax.inject.Named

@Module
class UseCaseModule {
    @Named("UseCaseScope")
    @Provides
    fun provideCoroutineScope() = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Named("UseCaseDispatcher")
    @Provides
    fun provideCoroutineDispatcher() : CoroutineDispatcher = Dispatchers.Main
}