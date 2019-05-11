package com.example.presentation.restaurants.presenter

import com.example.domain.ApplicationProvider
import com.example.domain.PlacesInteractor
import com.example.model.Restaurant
import com.example.presentation.restaurants.view.MapsView
import com.example.utils.Logger
import com.example.utils.PermissionsUtils
import com.example.utils.rx.SchedulersProviderStub
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@DisplayName("MapsViewPresenter tests")
internal class MapsViewPresenterTest {

    lateinit var mapsViewPresenter: MapsViewPresenter

    @Mock
    lateinit var logger: Logger
    @Mock
    lateinit var placesInteractor: PlacesInteractor
    @Mock
    lateinit var applicationProvider: ApplicationProvider
    @Mock
    lateinit var permissionsUtils: PermissionsUtils
    @Mock
    lateinit var mapsView: MapsView


    @BeforeEach
    fun beforeEachTest() {
        MockitoAnnotations.initMocks(this)
        setupPresenter()

        // TODO: create tests for permissions processing
        `when`(permissionsUtils.arePermissionGranted(anyString())).thenReturn(true)
    }

    @DisplayName("When PlacesInteractor returns restaurants")
    @Nested
    inner class PlacesInteractorReturnsRestaurants {
        @BeforeEach
        fun beforeEachTest() {
            `when`(placesInteractor.searchRestaurants(anyDouble(), anyDouble(), anyInt()))
                .thenReturn(Observable.just(restaurant1, restaurant2))
        }

        @Test
        @DisplayName("when map is moved, marker should be added to the map")
        fun addMarker() {
            mapsViewPresenter.onMapMoved(lat0, lng0, zoom)

            verify(mapsView).addMarker(restaurant1.id, LatLng(restaurant1.lat, restaurant1.lng), mapsViewPresenter.formatRestaurantDescription(restaurant1))
            verify(mapsView).addMarker(restaurant2.id, LatLng(restaurant2.lat, restaurant2.lng), mapsViewPresenter.formatRestaurantDescription(restaurant2))
        }
    }

    private fun setupPresenter() {
        mapsViewPresenter = MapsViewPresenter(logger, placesInteractor, applicationProvider, permissionsUtils, SchedulersProviderStub())
        mapsViewPresenter.attachView(mapsView)
    }

    private companion object {
        const val lat0 = 1.6
        const val lng0 = 1.9

        const val lat1 = 2.5
        const val lng1 = 1.2

        const val zoom = 21.2f

        const val radius = 200

        val restaurant1 = Restaurant("cecxwe", "sxwqswx", "xdewde", 33.3, 34.32)
        val restaurant2 = Restaurant("xdcer", "vrefer", "cregtr", 323.3, 444.32)
    }
}