package com.example.domain

import com.example.model.foursquare.details.FoursquareDetailsResult
import com.example.model.foursquare.search.FoursquareSearchResult
import io.reactivex.Single

interface FoursquareRepository {

    /**
     * Searches for restaurants near provided coordinates
     *
     * @param lat Latitude
     * @param lng Longitude
     * @param radius Radius in meters from the point
     * @return Single with FoursquareSearchResult
     *
     * Error: no
     * Subscribe: io
     */
    fun searchRestaurants(lat: Double, lng: Double, radius: Int) : Single<FoursquareSearchResult>

    /**
     * Returns details for a venue
     *
     * @param venueId ID of a Venue
     * @return Single with FoursquareDetailsResult for the venue
     *
     * Error: no
     * Subscribe: io
     */
    fun requestDetailsForVenue(venueId: String) : Single<FoursquareDetailsResult>

}