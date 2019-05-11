package com.example.data

import android.net.Uri
import javax.inject.Inject

class FoursquareUrlsGeneratorImpl @Inject constructor(): FoursquareUrlsGenerator {

    override fun createVenueDetailsRequestUrl(venueId: String): String {
        val builder = Uri.Builder()
        builder.scheme("https")
            .authority("api.foursquare.com")
            .appendPath("v2")
            .appendPath("venues")
            .appendPath(venueId)
            .appendQueryParameter("client_id", "SQKJ0TXEPU1TRNRVRL1FNEBYGBQU0RBZQLFJ5HNASJBL4TFG")
            .appendQueryParameter("client_secret", "GR2EKW35DQZCWHE5XKJAXPO2K2GMRQA5AZPZASFRSK21VART")
            .appendQueryParameter("v", "20190509")

        return builder.build().toString()
    }

    override fun createRestaurantRequestUrl(lat: Double, lng: Double, radius: Int): String {
        val builder = Uri.Builder()
        builder.scheme("https")
            .authority("api.foursquare.com")
            .appendPath("v2")
            .appendPath("venues")
            .appendPath("search")
            .appendQueryParameter("client_id", "SQKJ0TXEPU1TRNRVRL1FNEBYGBQU0RBZQLFJ5HNASJBL4TFG")
            .appendQueryParameter("client_secret", "GR2EKW35DQZCWHE5XKJAXPO2K2GMRQA5AZPZASFRSK21VART")
            .appendQueryParameter("v", "20190509")
            .appendQueryParameter("ll", "$lat,$lng")
            .appendQueryParameter("categoryId", "4d4b7105d754a06374d81259") // Restaurants
            .appendQueryParameter("intent", "browse")
            .appendQueryParameter("radius", "$radius")

        return builder.build().toString()
    }
}