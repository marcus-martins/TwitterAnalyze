package com.br.marcusmartins.twitteranalyze.core.di

import com.br.marcusmartins.twitteranalyze.AndroidApplication
import com.br.marcusmartins.twitteranalyze.core.di.usecase.UseCaseModule
import com.br.marcusmartins.twitteranalyze.core.di.viewmodel.ViewModelModule
import com.br.marcusmartins.twitteranalyze.core.navigation.RouteActivity
import com.br.marcusmartins.twitteranalyze.features.search.SearchFragment
import com.br.marcusmartins.twitteranalyze.features.tweets.TweetsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class, UseCaseModule::class])
interface ApplicationComponent {
    fun inject(application: AndroidApplication)
    fun inject(routeActivity: RouteActivity)

    fun inject(searchFragment: SearchFragment)
    fun inject(tweetsFragment: TweetsFragment)
}
