package com.urgentx.recycledump.model

import android.util.Log
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.urgentx.recycledump.model.callbacks.PlaceCallback
import com.urgentx.recycledump.util.Place


class PlacesApiInteractor {

    private val database = FirebaseDatabase.getInstance()

    fun retrievePlaces(latitude: Double, longitude: Double, callback: PlaceCallback) {
        val placeLocationsRef = database.getReference("placelocations")
        val geoFire = GeoFire(placeLocationsRef)
        val places = ArrayList<Place>()
        var numPlaces = 0
        var placesRetrieved = 0

        geoFire.queryAtLocation(GeoLocation(latitude, longitude), 100.0).addGeoQueryEventListener(object : GeoQueryEventListener {
            override fun onGeoQueryReady() {
            }

            override fun onKeyEntered(key: String?, location: GeoLocation?) {
                val locationQuery = database.getReference("places").child(key)
                locationQuery.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot?) {
                        val place = p0?.getValue(Place::class.java)
                        if (place != null) {
                            numPlaces ++
                            //Get place image and add to Place object
                            FirebaseStorage.getInstance().reference.child("placepics").child(key + ".jpg").downloadUrl
                                    .addOnSuccessListener { uri ->
                                        place.img = uri.toString()
                                        places.add(place)
                                        placesRetrieved ++
                                        if(placesRetrieved >= numPlaces){
                                            callback.placesRetrieved(places)
                                        }
                                    }
                                    .addOnFailureListener({
                                        placesRetrieved ++
                                        place.img = ""
                                        places.add(place)
                                        if(placesRetrieved >= numPlaces) {
                                            callback.placesRetrieved(places)
                                        }
                                    })
                        }
                    }

                    override fun onCancelled(p0: DatabaseError?) {
                        Log.d("firebase", p0.toString())
                    }
                })
            }

            override fun onKeyMoved(key: String?, location: GeoLocation?) {
            }

            override fun onKeyExited(key: String?) {
            }

            override fun onGeoQueryError(error: DatabaseError?) {
            }

        })
    }
}