package com.example.presentation.restaurants.presenter

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v4.content.ContextCompat
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.domain.ApplicationProvider
import com.example.domain.PlacesErrorCode
import com.example.domain.PlacesException
import com.example.domain.PlacesInteractor
import com.example.model.Restaurant
import com.example.model.RestaurantDetails
import com.example.model.RestaurantDetailsRequestResultCode
import com.example.presentation.restaurants.R
import com.example.presentation.restaurants.view.MapsView
import com.example.utils.Logger
import com.example.utils.PermissionsUtils
import com.example.utils.rx.SchedulersProvider
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@InjectViewState
class MapsViewPresenter
    @Inject constructor(
        val logger: Logger,
        val placesInteractor: PlacesInteractor,
        val applicationProvider: ApplicationProvider,
        val permissionsUtils: PermissionsUtils,
        val schedulersProvider: SchedulersProvider
    ) : MvpPresenter<MapsView>(), LocationListener {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val newCoordinatesObservable : PublishSubject<MapPosition> = PublishSubject.create()

    private var locationManager: LocationManager? = null

    fun onMapReady() {
        if (!areRequiredPermissionsGranted()) {
            viewState.requestPermissions(arrayOf(LOCATION_PERMISSION))
        } else {
            viewState.setMyLocationEnabled(true)
            val location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                val latLng = LatLng(location.getLatitude(), location.getLongitude());
                viewState.setLocation(latLng, DEFAULT_ZOOM)
            }
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        locationManager = applicationProvider.locationManager
        if (areRequiredPermissionsGranted()) {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1000f, this)
        }

        val searchRestaurantsDisposable: Disposable = newCoordinatesObservable.flatMap { mapPosition: MapPosition ->
                if (mapPosition.zoom > MINIMUM_ZOOM_FOR_SEARCH) {
                    placesInteractor.searchRestaurants(mapPosition.lat, mapPosition.lng, 250)
                } else
                    Observable.error(RuntimeException("Skip search"))
            }
            .observeOn(schedulersProvider.main())
            .doOnNext  { restaurant ->
                viewState.addMarker(restaurant.id, LatLng(restaurant.lat, restaurant.lng), formatRestaurantDescription(restaurant))
            }
            .doOnError { throwable: Throwable? ->
                if (throwable is PlacesException) {
                    if (throwable.errorCode == PlacesErrorCode.NO_CONNECTION) {
                        viewState.showNoConnectionError()
                    }
                }
            }
            .retry()
            .subscribe({ restaurant -> logger.i(TAG, "onMapMoved() got restaurant: ${restaurant}") },
                { throwable: Throwable -> logger.w(TAG, "onMapMoved()", throwable) })

        compositeDisposable.add(searchRestaurantsDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    fun onMapMoved(lat: Double, lng: Double, zoom: Float) {
        newCoordinatesObservable.onNext(MapPosition(lat, lng, zoom))
    }

    fun onMarkerClicked(marker: Marker?): Boolean  {
        val requestDetailsDisposable: Disposable =
            Single.defer {
                 if (marker == null) {
                     Single.error(RuntimeException(""))
                 } else {
                    Single.just(marker.tag as String)
                 }
            }
            .flatMap { id -> placesInteractor.requestRestaurantDetails(id) }
            .flatMap { restaurantDetailsRequestResult ->
                if (restaurantDetailsRequestResult.code != RestaurantDetailsRequestResultCode.OK) {
                    Single.error(RuntimeException("Request failed"))
                } else {
                    Single.just(restaurantDetailsRequestResult.details)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { throwable: Throwable? ->
                if (throwable is PlacesException) {
                    if (throwable.errorCode == PlacesErrorCode.NO_CONNECTION) {
                        viewState.showNoConnectionError()
                    }
                }
            }
            .doOnSubscribe { viewState.showProgress(true) }
            .doFinally { viewState.showProgress(false) }
            .doOnError { viewState.showGeneralError() }
            .subscribe( { details->
                    viewState.showRestaurantsDetails(details?.name?:"", formatRestaurantDetails(details)) },
                { throwable -> logger.w(TAG, "onMarkerClicked()", throwable) })

        compositeDisposable.add(requestDetailsDisposable)

        return true
    }

    fun onMyLocationButtonClick(): Boolean {
        if (areRequiredPermissionsGranted()) {
            return false
        }
        viewState.requestPermissions(arrayOf(LOCATION_PERMISSION))
        return true
    }

    fun onPermissionsResult(permissions: Array<out String>, grantResults: IntArray) {
        if (permissions[0] == LOCATION_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewState.setMyLocationEnabled(true)
        }
    }

    private fun formatRestaurantDetails(restaurantDetails: RestaurantDetails?): String {
        return restaurantDetails?.description?: applicationProvider.applicationContext.getString(R.string.dialog_empty_restaurant_description_message) +
            "\n\n" + if (restaurantDetails?.url != null) restaurantDetails.url else "" +
            "\n\n" + if (restaurantDetails?.rating != null) applicationProvider.applicationContext.getString(R.string.dialog_rating_message) + "${restaurantDetails.rating}" else ""
    }

    @VisibleForTesting
    open fun formatRestaurantDescription(restaurant: Restaurant): String {
        return restaurant.name + (if (!restaurant.address.isBlank()) " @ ${restaurant.address}" else "" )
    }

    private fun areRequiredPermissionsGranted(): Boolean {
        return permissionsUtils.arePermissionGranted(LOCATION_PERMISSION)
    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            val latLng = LatLng(location.getLatitude(), location.getLongitude());
            viewState.setLocation(latLng, DEFAULT_ZOOM)
            locationManager?.removeUpdates(this);
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    private data class MapPosition (
        val lat: Double,
        val lng: Double,
        val zoom: Float
    )

    companion object {
        private const val TAG: String = "MapsViewPresenter"
        const val LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION
        const val DEFAULT_ZOOM = 20f
        const val MINIMUM_ZOOM_FOR_SEARCH = 10f
    }
}