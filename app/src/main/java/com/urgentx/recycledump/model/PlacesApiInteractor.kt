package com.urgentx.recycledump.model

import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.urgentx.recycledump.model.callbacks.BinaryCallback
import com.urgentx.recycledump.model.callbacks.PlacesCallback
import com.urgentx.recycledump.util.Place


class PlacesApiInteractor {

    private val database = FirebaseDatabase.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    fun savePlace(place: Place, callback: BinaryCallback) {
        val placeLocationsRef = database.getReference("placelocations")
        val geoFire = GeoFire(placeLocationsRef)

        geoFire.setLocation(place.name, GeoLocation(place.lat, place.long),
                { key, error ->
                    if (error != null) {
                        callback.onBinaryError()
                    } else {
                        callback.onBinarySuccess()
                    }
                })
    }

    fun getPlaces(callback: PlacesCallback) {

    }
}