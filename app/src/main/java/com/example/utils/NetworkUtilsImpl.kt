package com.example.utils

import android.content.Context
import android.net.ConnectivityManager
import com.example.domain.ApplicationProvider
import javax.inject.Inject

class NetworkUtilsImpl
    @Inject
    constructor(
        val applicationProvider: ApplicationProvider,
        val logger: Logger
    ) : NetworkUtils {

    override val networkConnectionOn: Boolean
        get() = isNetworkConnectionOn()

    private fun isNetworkConnectionOn(): Boolean {
        logger.i(TAG, "isNetworkConnectionOn() checking...")
        var on = false
        val cm = applicationProvider.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (cm != null) {
            val activeNetwork = cm.getActiveNetworkInfo()
            if (activeNetwork != null)
                on = activeNetwork.isConnected() //check any available network
        }
        logger.i(TAG, "isNetworkConnectionOn() network is ${if (on) "on" else "off"}")
        return on
    }

    private companion object {
        private val TAG: String = "NetworkUtils"
    }
}