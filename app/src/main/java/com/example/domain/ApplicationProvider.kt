package com.example.domain

import android.content.Context
import android.location.LocationManager
import com.example.TheApplication

interface ApplicationProvider {
    /**
     * Initializes ApplicationProvider. Must be called before any interaction with ApplicationProvider.
     *
     * @param theApplication application object
     */
    fun init(theApplication: TheApplication)

    /**
     * @returns application context
     */
    val applicationContext: Context

    val locationManager: LocationManager
}