package com.example.presentation.restaurants.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.google.android.gms.maps.model.LatLng

interface MapsView: MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun addMarker(tag: String, latLng: LatLng, detail: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setLocation(latLng: LatLng, zoom: Float)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showNoConnectionError()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showGeneralError()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showRestaurantsDetails(title: String, details: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setMyLocationEnabled(enabled: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun requestPermissions(permissions: Array<String>)
}