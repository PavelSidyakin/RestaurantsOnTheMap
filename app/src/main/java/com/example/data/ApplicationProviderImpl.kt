package com.example.data

import android.content.Context
import android.location.LocationManager
import com.example.TheApplication
import com.example.domain.ApplicationProvider
import javax.inject.Inject

class ApplicationProviderImpl @Inject constructor() : ApplicationProvider {
    private lateinit var theApplication: TheApplication

    override fun init(theApplication: TheApplication) {
        this.theApplication = theApplication
    }

    override val applicationContext: Context
        get() = theApplication.applicationContext

    override val locationManager: LocationManager
        get() = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

}