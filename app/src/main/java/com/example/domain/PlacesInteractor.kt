package com.example.domain

import com.example.model.Restaurant
import com.example.model.RestaurantDetailsRequestResult
import io.reactivex.Observable
import io.reactivex.Single

interface PlacesInteractor {

    /**
     * Searches for restaurants near provided coordinates
     *
     * @param lat Latitude
     * @param lng Longitude
     * @param radius Radius in meters from the point
     * @return Observable which emits [Restaurant]
     *
     * Error: [PlacesException]
     * Subscribe: io
     */
    fun searchRestaurants(lat: Double, lng: Double, radius: Int): Observable<Restaurant>

    /**
     * Returns details for a venue
     *
     * @param id ID of a restaurant
     * @return Single with RestaurantDetailsRequestResult for the restaurant
     *
     * Error: no
     * Subscribe: io
     */
    fun requestRestaurantDetails(id: String): Single<RestaurantDetailsRequestResult>

}