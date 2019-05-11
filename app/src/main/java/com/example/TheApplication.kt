package com.example

import android.app.Application
import com.example.di.AppComponent
import com.example.di.DaggerAppComponent
import com.example.domain.ApplicationProvider
import javax.inject.Inject

class TheApplication : Application() {
    @Inject
    lateinit var applicationProvider: ApplicationProvider

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .build()

        appComponent.inject(this)

        applicationProvider.init(this)
    }

    companion object {
        private lateinit var appComponent: AppComponent

        fun getAppComponent(): AppComponent {
            return appComponent
        }
    }
}