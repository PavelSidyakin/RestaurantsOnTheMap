package com.example.presentation.restaurants.view

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.support.annotation.RequiresPermission
import android.support.v4.app.ActivityCompat
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.TheApplication
import com.example.domain.ApplicationProvider
import com.example.domain.PlacesInteractor
import com.example.presentation.restaurants.R
import com.example.presentation.restaurants.presenter.MapsViewPresenter
import com.example.utils.Logger
import com.example.utils.PermissionsUtils
import com.example.utils.rx.SchedulersProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class MapsActivity : MvpAppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraChangeListener, MapsView, GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener {
    private lateinit var map: GoogleMap

    @InjectPresenter
    lateinit var mapsViewPresenter: MapsViewPresenter

    @Inject
    lateinit var placesInteractor: PlacesInteractor
    @Inject
    lateinit var logger: Logger
    @Inject
    lateinit var applicationProvider: ApplicationProvider
    @Inject
    lateinit var permissionsUtils: PermissionsUtils
    @Inject
    lateinit var schedulersProvider: SchedulersProvider

    private lateinit var progressDialog: ProgressDialog


    @ProvidePresenter
    internal fun providePresenter(): MapsViewPresenter {
        return MapsViewPresenter(logger, placesInteractor, applicationProvider, permissionsUtils, schedulersProvider)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        TheApplication.getAppComponent().inject(this)

        super.onCreate(savedInstanceState)

        progressDialog = ProgressDialog(this)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setIndeterminate(true)

        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Sorry for that.
        // I understand that using deprecated API is a bad idea.
        // Used here for simplification.
        map.setOnCameraChangeListener(this)

        map.setOnMarkerClickListener(this)
        map.setOnMyLocationButtonClickListener(this)

        mapsViewPresenter.onMapReady()
    }

    override fun setLocation(latLng: LatLng, zoom: Float) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
        mapsViewPresenter.onMapMoved(latLng.latitude, latLng.longitude, zoom)
    }

    override fun addMarker(tag: String, latLng: LatLng, detail: String) {
        map.addMarker(MarkerOptions().position(latLng).title(detail)).tag = tag
    }

    override fun onCameraChange(p0: CameraPosition?) {
        logger.i(TAG, "onCameraChange() $p0")
        mapsViewPresenter.onMapMoved(p0?.target?.latitude?:0.0, p0?.target?.longitude?:0.0, p0?.zoom?:0f)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return mapsViewPresenter.onMarkerClicked(p0)
    }

    override fun onMyLocationButtonClick(): Boolean {
        return mapsViewPresenter.onMyLocationButtonClick()
    }

    override fun requestPermissions(permissions: Array<String>) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, MapsViewPresenter.LOCATION_PERMISSION)) {
            showAllowPermissionsInSettings()
        } else {
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_LOCATION_REQUEST_CODE);
        }
    }

    override fun showGeneralError() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage(R.string.dialog_general_error_message)
        dialogBuilder.setPositiveButton(R.string.dialog_ok_button, null)
        dialogBuilder.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_LOCATION_REQUEST_CODE) {
            mapsViewPresenter.onPermissionsResult(permissions, grantResults)
        }
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }

    override fun showRestaurantsDetails(title: String, details: String) {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(details)
        dialogBuilder.setPositiveButton(R.string.dialog_ok_button, null)
        dialogBuilder.show()
    }

    fun showAllowPermissionsInSettings() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage(R.string.dialog_grant_permissions_in_settings_message)
        dialogBuilder.setPositiveButton(R.string.dialog_ok_button, null)
        dialogBuilder.show()
    }

    override fun showNoConnectionError() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage(R.string.dialog_no_connection_message)
        dialogBuilder.setPositiveButton(R.string.dialog_ok_button, null)
        dialogBuilder.show()
    }

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    override fun setMyLocationEnabled(enabled: Boolean) {
        map.setMyLocationEnabled(enabled)
    }

    private companion object {
        const val TAG: String = "MapsActivity"
        const val PERMISSIONS_REQUEST_LOCATION_REQUEST_CODE = 1;
    }

}
