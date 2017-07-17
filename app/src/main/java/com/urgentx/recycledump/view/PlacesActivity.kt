package com.urgentx.recycledump.view

import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.urgentx.recycledump.R
import com.urgentx.recycledump.presenter.PlacesPresenter
import com.urgentx.recycledump.util.Place
import com.urgentx.recycledump.view.IView.IPlacesView

class PlacesActivity : FragmentActivity(), OnMapReadyCallback, IPlacesView {

    private var mMap: GoogleMap? = null
    private var presenter: PlacesPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if(presenter == null){
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

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-36.8823072, 174.71699050000007)
        mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        presenter?.savePlace(Place("Z Castle", 1, 2, sydney.latitude, sydney.longitude))
    }

    override fun placesRetrieved() {

    }

    override fun placeSaved() {
        Toast.makeText(this, "Place saved.", Toast.LENGTH_LONG).show()
    }

    override fun errorOccurred() {
    }
}
