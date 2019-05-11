package com.example.domain

import com.example.model.Restaurant
import com.example.model.RestaurantDetails
import com.example.model.RestaurantDetailsRequestResult
import com.example.model.RestaurantDetailsRequestResultCode
import com.example.model.foursquare.details.FoursquareDetailsResultCode
import com.example.model.foursquare.search.FoursquareSearchResultCode
import com.example.utils.Logger
import com.example.utils.NetworkUtils
import com.example.utils.rx.SchedulersProvider
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class PlacesInteractorImpl
    @Inject
    constructor(
        val logger: Logger,
        val foursquareRepository: FoursquareRepository,
        val schedulersProvider: SchedulersProvider,
        val networkUtils: NetworkUtils)
    : PlacesInteractor {

    override fun searchRestaurants(lat: Double, lng: Double, radius: Int): Observable<Restaurant> {
        return Single.fromCallable { networkUtils.networkConnectionOn }
            .flatMap { on ->
                if (on) {
                    foursquareRepository.searchRestaurants(lat, lng, radius)
                }
                else {
                    Single.error(PlacesException(PlacesErrorCode.NO_CONNECTION))
                }
            }
            .toObservable()
            .flatMap { foursquareSearchResult ->
                if (foursquareSearchResult.resultCode != FoursquareSearchResultCode.OK) {
            Observable.error(PlacesException(PlacesErrorCode.GENERAL_ERROR))
                } else
            Observable.just(foursquareSearchResult.foursquareSearchResponseData)
            }
            .flatMap { foursquareSearchResponseData ->
                if (foursquareSearchResponseData.response != null) {
                    Observable.fromIterable(foursquareSearchResponseData.response.venues)
                } else
                    Observable.error(PlacesException(PlacesErrorCode.GENERAL_ERROR))
            }
            .map { venue -> convertVenueToRestaurant(venue) }
            .subscribeOn(schedulersProvider.io())
            .doOnSubscribe { logger.i(TAG, "searchRestaurants() subscribe") }
            .doOnError { throwable ->  logger.w(TAG, "searchRestaurants() error", throwable) }
            .doOnNext { restaurant ->  logger.i(TAG, "searchRestaurants() next $restaurant") }
    }

    private fun convertVenueToRestaurant(venue: com.example.model.foursquare.search.response.Venue) : Restaurant {
        return Restaurant(venue.id ?:"",
            venue.name?:"",
            venue.location?.address?:"",
            venue.location?.lat?:0.0,
            venue.location?.lng?:0.0)
    }

    override fun requestRestaurantDetails(id: String): Single<RestaurantDetailsRequestResult> {
        return Single.fromCallable { networkUtils.networkConnectionOn }
            .flatMap { on ->
                if (on) {
                    foursquareRepository.requestDetailsForVenue(id)
                }
                else {
                    Single.error(PlacesException(PlacesErrorCode.NO_CONNECTION))
                }
            }
            .flatMap { foursquareDetailsResult ->
                if (foursquareDetailsResult.code != FoursquareDetailsResultCode.OK) {
                    Single.error(PlacesException(PlacesErrorCode.GENERAL_ERROR))
                } else {
                    Single.just(foursquareDetailsResult.foursquareDetailsResponseData)
                }
            }
            .flatMap { foursquareDetailsResponseData ->
                if (foursquareDetailsResponseData.response != null) {
                    Single.just(foursquareDetailsResponseData.response.venue)
                } else {
                    Single.error(PlacesException(PlacesErrorCode.GENERAL_ERROR))
                }
            }
            .map { venue -> RestaurantDetailsRequestResult(RestaurantDetailsRequestResultCode.OK, convertVenueToRestaurantDetails(venue)) }
            .onErrorResumeNext { throwable ->
                if (throwable is PlacesException && throwable.errorCode == PlacesErrorCode.NO_CONNECTION) {
                Single.just(RestaurantDetailsRequestResult(RestaurantDetailsRequestResultCode.NO_CONNECTION, null))
                } else
                Single.just(RestaurantDetailsRequestResult(RestaurantDetailsRequestResultCode.GENERAL_ERROR, null))
            }
            .subscribeOn(schedulersProvider.io())
    }

    private fun convertVenueToRestaurantDetails(venue: com.example.model.foursquare.details.response.Venue) : RestaurantDetails {
        return RestaurantDetails(
            venue.name?:"",
            venue.description,
            venue.url,
            venue.rating)
    }

    private companion object {
        const val TAG = "PlacesInteractor"
    }

}
