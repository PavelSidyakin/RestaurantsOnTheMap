package com.example.data

interface FoursquareUrlsGenerator {
    fun createRestaurantRequestUrl(lat: Double, lng: Double, radius: Int): String
    fun createVenueDetailsRequestUrl(venueId: String): String
}