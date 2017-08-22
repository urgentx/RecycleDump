package com.urgentx.recycledump.view

import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.urgentx.recycledump.R
import com.urgentx.recycledump.presenter.PlacesPresenter
import com.urgentx.recycledump.util.Place
import com.urgentx.recycledump.view.IView.IPlacesView


class PlacesActivity : FragmentActivity(), OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, IPlacesView, AddPlaceFragment.OnFragmentInteractionListener, PlaceFragment.OnFragmentInteractionListener{


    private var mMap: GoogleMap? = null
    private var presenter: PlacesPresenter? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var places = ArrayList<Place>() //Holds places retrieved, use to build place dialog.

    private var mLastKnownLocation: Location? = null
    private var mCameraPosition: CameraPosition? = null

    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private val DEFAULT_ZOOM = 1.0f

    private var mLocationPermissionGranted = false
    private var locationRetrieved = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations)

        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build()
        mGoogleApiClient?.connect()

        if (presenter == null) {
            presenter = PlacesPresenter()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter?.onViewAttached(this)
    }

    override fun onPause() {
        super.onPause()
        presenter?.onViewDetached()
    }

    override fun onConnected(p0: Bundle?) {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
        }
        updateLocationUI()

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()

        mMap?.setOnMapClickListener({ point ->
            val addPlaceFragment = AddPlaceFragment.newInstance(point.latitude, point.longitude)
            addPlaceFragment.show(supportFragmentManager, "addplacefragtag")
            mMap?.addMarker(MarkerOptions().position(point))
        })

        mMap?.setOnMarkerClickListener { marker ->
            places.filter {
                it.name == marker?.title
            }.map({
                PlaceFragment.newInstance(it.name, it.img)
            }).forEach {
                it.show(supportFragmentManager, "placefragtag")
            }
            true
        }
    }

    private fun updateLocationUI() {
        /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }

        if (mLocationPermissionGranted) {
            mMap?.isMyLocationEnabled = true
            mMap?.uiSettings?.isMyLocationButtonEnabled = true
        } else {
            mMap?.isMyLocationEnabled = false
            mMap?.uiSettings?.isMyLocationButtonEnabled = false
            mLastKnownLocation = null
        }

        getDeviceLocation()
    }

    private fun getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient)
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap?.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition))
        } else if (mLastKnownLocation != null) {
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    LatLng((mLastKnownLocation as Location).latitude,
                            (mLastKnownLocation as Location).longitude), DEFAULT_ZOOM))
            if(!locationRetrieved){
                locationRetrieved = true
                presenter?.retrievePlaces((mLastKnownLocation as Location).latitude, (mLastKnownLocation as Location).longitude)
            }
        } else {
            Log.d("LocationActivity", "Current location is null. Using defaults.")
//            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(36.8482, 174.8318), DEFAULT_ZOOM))
//            presenter?.retrievePlaces(36.8482, 174.8318)
//            mMap?.uiSettings?.isMyLocationButtonEnabled = false
        }
    }

    override fun placeRetrieved(place: Place) {
        places.add(place)
        addPlaceMarker(place)
    }


    override fun placesRetrieved(places: ArrayList<Place>) {
        this.places.addAll(places)
        for(place in places){
            addPlaceMarker(place)
        }
    }

    private fun addPlaceMarker(place: Place){
        var color : Float = BitmapDescriptorFactory.HUE_ORANGE
        if(place.type == 0) {
            color = BitmapDescriptorFactory.HUE_GREEN
        }
        mMap?.addMarker(MarkerOptions().position(LatLng(place.lat, place.long))
                .title(place.name)
                .icon(BitmapDescriptorFactory.defaultMarker(color))
        )
    }


    override fun errorOccurred() {
    }

    override fun onFragmentInteraction(uri: Uri) {
    }
}

