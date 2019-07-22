package com.br.marcusmartins.twitteranalyze.core.di.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.br.marcusmartins.twitteranalyze.features.tweets.TweetsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TweetsViewModel::class)
    abstract fun bindsTweetsViewModel(tweetsViewModel: TweetsViewModel): ViewModel
}