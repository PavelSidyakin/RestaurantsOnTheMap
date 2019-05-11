package com.example.di

import com.example.TheApplication
import com.example.presentation.restaurants.view.MapsActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun inject(mapsActivity: MapsActivity)
    fun inject(mapsActivity: TheApplication)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}