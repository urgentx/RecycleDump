package com.urgentx.recycledump.model

import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.urgentx.recycledump.model.callbacks.BinaryCallback
import com.urgentx.recycledump.model.callbacks.PlaceCallback
import com.urgentx.recycledump.util.Place


class AddPlaceApiInteractor {

    private val database = FirebaseDatabase.getInstance()

    fun savePlace(place: Place, callback: BinaryCallback) {
        val placeLocationsRef = database.getReference("placelocations") //Geofire directory
        val placesRef = database.getReference("places")
        val geoFire = GeoFire(placeLocationsRef)

        geoFire.setLocation(place.name, GeoLocation(place.lat, place.long),
                { key, error ->
                    if (error != null) {
                        callback.onBinaryError()
                    } else {
                        placesRef.child(key).setValue(place).addOnCompleteListener(
                                { callback.onBinarySuccess() })
                                .addOnFailureListener({
                                    callback.onBinaryError()
                                })
                    }
                })
    }

    fun getPlaces(callback: PlaceCallback) {

    }
}