package com.br.marcusmartins.twitteranalyze

import android.app.Application
import com.br.marcusmartins.twitteranalyze.core.di.ApplicationComponent
import com.br.marcusmartins.twitteranalyze.core.di.ApplicationModule
import com.br.marcusmartins.twitteranalyze.core.di.DaggerApplicationComponent

class AndroidApplication : Application() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
    }

    private fun injectMembers() = appComponent.inject(this)
}