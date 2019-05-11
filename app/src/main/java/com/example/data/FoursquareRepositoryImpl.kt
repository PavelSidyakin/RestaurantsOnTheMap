package com.example.data

import com.example.domain.FoursquareRepository
import com.example.model.foursquare.details.FoursquareDetailsResult
import com.example.model.foursquare.details.FoursquareDetailsResultCode
import com.example.model.foursquare.details.response.FoursquareDetailsResponseData
import com.example.model.foursquare.search.FoursquareSearchResult
import com.example.model.foursquare.search.FoursquareSearchResultCode
import com.example.model.foursquare.search.response.FoursquareSearchResponseData
import com.example.utils.HttpClient
import com.example.utils.Logger
import com.example.utils.rx.SchedulersProvider
import com.google.gson.Gson
import io.reactivex.Single
import javax.inject.Inject

class FoursquareRepositoryImpl
    @Inject constructor(
        val logger: Logger,
        val httpClient: HttpClient,
        val foursquareUrlsGenerator: FoursquareUrlsGenerator,
        val schedulersProvider: SchedulersProvider
    )
    : FoursquareRepository {

    override fun searchRestaurants(lat: Double, lng: Double, radius: Int): Single<FoursquareSearchResult> {
        return httpClient.executeGet(foursquareUrlsGenerator.createRestaurantRequestUrl(lat, lng, radius))
            .flatMap { response ->
                if (response.isBlank())
                    Single.error(RuntimeException("Empty response"))
                else
                    Single.just(Gson().fromJson(response, FoursquareSearchResponseData::class.java))
            }
            .flatMap { foursquareSearchResponseData ->
                    if (foursquareSearchResponseData.meta == null || foursquareSearchResponseData.meta.code != 200) {
                Single.error(RuntimeException("Unexpected result"))
                    } else
                Single.just(foursquareSearchResponseData)
            }
            .map { foursquareSearchResponseData ->
                FoursquareSearchResult(FoursquareSearchResultCode.OK, foursquareSearchResponseData)
            }
            .onErrorResumeNext { Single.just(FoursquareSearchResult(FoursquareSearchResultCode.GENERAL_ERROR, null)) }
            .subscribeOn(schedulersProvider.io())
            .doOnSubscribe { logger.i(TAG, "searchRestaurants() subscribe") }
            .doOnError { throwable ->  logger.w(TAG, "searchRestaurants() error", throwable) }
            .doOnSuccess { result -> logger.i(TAG, "searchRestaurants() next $result") }

    }

    override fun requestDetailsForVenue(venueId: String): Single<FoursquareDetailsResult> {
        return httpClient.executeGet(foursquareUrlsGenerator.createVenueDetailsRequestUrl(venueId))
            .flatMap { response ->
                if (response.isBlank())
                    Single.error(RuntimeException("Empty response"))
                else
                    Single.just(Gson().fromJson(response, FoursquareDetailsResponseData::class.java))
            }
            .flatMap { foursquareDetailsResponseData ->
                if (foursquareDetailsResponseData.meta == null || foursquareDetailsResponseData.meta.code != 200) {
                    Single.error(RuntimeException("Unexpected result"))
                } else
                    Single.just(foursquareDetailsResponseData)
            }
            .map { foursquareDetailsResponseData ->
                FoursquareDetailsResult(FoursquareDetailsResultCode.OK, foursquareDetailsResponseData)
            }
            .onErrorResumeNext { Single.just(FoursquareDetailsResult(FoursquareDetailsResultCode.GENERAL_ERROR, null)) }
            .subscribeOn(schedulersProvider.io())
    }

    private companion object {
        const val TAG = "FoursquareRepo"
    }
}