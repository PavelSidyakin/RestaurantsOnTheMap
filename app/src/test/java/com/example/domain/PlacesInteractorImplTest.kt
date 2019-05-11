package com.example.domain

import com.example.model.RestaurantDetailsRequestResult
import com.example.model.RestaurantDetailsRequestResultCode
import com.example.model.foursquare.details.FoursquareDetailsResult
import com.example.model.foursquare.details.FoursquareDetailsResultCode
import com.example.model.foursquare.details.response.FoursquareDetailsResponseData
import com.example.model.foursquare.search.FoursquareSearchResult
import com.example.model.foursquare.search.FoursquareSearchResultCode
import com.example.model.foursquare.search.response.FoursquareSearchResponseData
import com.example.utils.Logger
import com.example.utils.NetworkUtils
import com.example.utils.rx.SchedulersProvider
import com.example.utils.rx.SchedulersProviderStub
import io.reactivex.Single
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyDouble
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@DisplayName("PlacesInteractorImpl tests")
internal class PlacesInteractorImplTest {

    @Mock
    private lateinit var logger: Logger
    @Mock
    private lateinit var foursquareRepository: FoursquareRepository
    @Mock
    private lateinit var networkUtils: NetworkUtils

    private lateinit var schedulersProvider: SchedulersProvider

    private lateinit var placesInteractorImpl: PlacesInteractorImpl

    @BeforeEach
    fun beforeEachTest() {
        MockitoAnnotations.initMocks(this)

        schedulersProvider = SchedulersProviderStub()

        placesInteractorImpl = PlacesInteractorImpl(logger,
            foursquareRepository,
            schedulersProvider,
            networkUtils)

    }

    @DisplayName("When no internet")
    @Nested
    inner class NoInternet {
        @BeforeEach
        fun beforeEachTest() {
            `when`(networkUtils.networkConnectionOn).thenReturn(false)
        }

        @Test
        @DisplayName("searchRestaurants() should emit error PlacesErrorCode.NO_CONNECTION")
        fun searchRestaurants() {
            placesInteractorImpl.searchRestaurants(lat, lng, radius)
                .test()
                .await()
                .assertNotComplete()
                .assertError(PlacesException(PlacesErrorCode.NO_CONNECTION))

            verify(foursquareRepository, never()).searchRestaurants(anyDouble(), anyDouble(), anyInt())
        }

        @Test
        @DisplayName("requestRestaurantDetails() should emit result with PlacesErrorCode.NO_CONNECTION error")
        fun requestRestaurantDetails() {
            placesInteractorImpl.requestRestaurantDetails(restaurantId)
                .test()
                .await()
                .assertComplete()
                .assertResult(RestaurantDetailsRequestResult(RestaurantDetailsRequestResultCode.NO_CONNECTION, null))

            verify(foursquareRepository, never()).requestDetailsForVenue(anyString())
        }

    }

    @DisplayName("When has internet")
    @Nested
    inner class HasInternet {
        @BeforeEach
        fun beforeEachTest() {
            `when`(networkUtils.networkConnectionOn).thenReturn(true)
        }


        @DisplayName("and foursquareRepository.searchRestaurants() returned OK")
        @Nested
        inner class FoursquareRepository_searchRestaurants_Ok {
            @BeforeEach
            fun beforeEachTest() {

                val venues = List(2) {index ->
                    when(index) {
                        0 -> com.example.model.foursquare.search.response.Venue(venue0Id)
                        1 -> com.example.model.foursquare.search.response.Venue(venue1Id)
                        else -> com.example.model.foursquare.search.response.Venue(null)
                    }
                }

                val foursquareSearchResponseData = FoursquareSearchResponseData(null,
                    com.example.model.foursquare.search.response.Response(venues)
                )

                `when`(foursquareRepository.searchRestaurants(lat, lng, radius)).thenReturn(Single.just(
                    FoursquareSearchResult(FoursquareSearchResultCode.OK, foursquareSearchResponseData)
                ))
            }

            @Test
            @DisplayName("searchRestaurants() should emit all restaurants")
            fun searchRestaurants() {
                placesInteractorImpl.searchRestaurants(lat, lng, radius)
                    .test()
                    .await()
                    .assertValueAt(0, { restaurant ->  restaurant.id == venue0Id})
                    .assertValueAt(1, { restaurant ->  restaurant.id == venue1Id})

            }

            @AfterEach
            fun afterEachTest() {
                verify(foursquareRepository).searchRestaurants(anyDouble(), anyDouble(), anyInt())
            }
        }

        @DisplayName("and foursquareRepository.requestDetailsForVenue() returned OK")
        @Nested
        inner class FoursquareRepository_requestDetailsForVenue_Ok {
            @BeforeEach
            fun beforeEachTest() {

                val foursquareDetailsResponseData = FoursquareDetailsResponseData(null,
                    com.example.model.foursquare.details.response.Response(
                        com.example.model.foursquare.details.response.Venue(
                            null, venueName
                        ))
                )

                `when`(foursquareRepository.requestDetailsForVenue(anyString())).thenReturn(Single.just(
                    FoursquareDetailsResult(FoursquareDetailsResultCode.OK, foursquareDetailsResponseData)
                ))
            }

            @Test
            @DisplayName("requestRestaurantDetails() should return the same venue")
            fun searchRestaurants() {
                placesInteractorImpl.requestRestaurantDetails(venue1Id)
                    .test()
                    .await()
                    .assertValue( { restaurantDetailsRequestResult ->
                        restaurantDetailsRequestResult.details?.name == venueName
                    })

            }

            @AfterEach
            fun afterEachTest() {
                verify(foursquareRepository).requestDetailsForVenue(anyString())
            }
        }


        @DisplayName("and foursquareRepository.searchRestaurants() returned error")
        @Nested
        inner class FoursquareRepository_searchRestaurants_Fail {
            @BeforeEach
            fun beforeEachTest() {

                `when`(foursquareRepository.searchRestaurants(lat, lng, radius)).thenReturn(Single.just(
                    FoursquareSearchResult(FoursquareSearchResultCode.GENERAL_ERROR, null)
                ))
            }

            @Test
            @DisplayName("searchRestaurants() should emit error")
            fun searchRestaurants() {
                placesInteractorImpl.searchRestaurants(lat, lng, radius)
                    .test()
                    .await()
                    .assertNotComplete()
                    .assertError( { throwable -> throwable is PlacesException })
            }

            @AfterEach
            fun afterEachTest() {
                verify(foursquareRepository).searchRestaurants(anyDouble(), anyDouble(), anyInt())
            }
        }

        @DisplayName("and foursquareRepository.requestDetailsForVenue() returned error")
        @Nested
        inner class FoursquareRepository_requestDetailsForVenue_Fail {
            @BeforeEach
            fun beforeEachTest() {

                `when`(foursquareRepository.requestDetailsForVenue(anyString())).thenReturn(Single.just(
                    FoursquareDetailsResult(FoursquareDetailsResultCode.GENERAL_ERROR, null)
                ))
            }

            @Test
            @DisplayName("requestRestaurantDetails() should return result with error")
            fun searchRestaurants() {
                placesInteractorImpl.requestRestaurantDetails(venue1Id)
                    .test()
                    .await()
                    .assertComplete()
                    .assertValue { result -> result.code == RestaurantDetailsRequestResultCode.GENERAL_ERROR }
            }

            @AfterEach
            fun afterEachTest() {
                verify(foursquareRepository).requestDetailsForVenue(anyString())
            }
        }

    }

    private companion object {
        const val lat = 1.0
        const val lng = 1.0
        const val radius = 1
        const val restaurantId = "chreufhrtrr7478r67e4y"

        const val venue0Id = "ecdwcexcew"
        const val venue1Id = "fedferfcerfre"

        const val venueName = "cnfjekvjirjvir"
    }
}