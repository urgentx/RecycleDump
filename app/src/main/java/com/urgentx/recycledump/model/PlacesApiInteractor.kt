package com.urgentx.recycledump.model

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

        val geoQuery = geoFire.queryAtLocation(GeoLocation(latitude, longitude), 100.0)

        geoQuery.addGeoQueryEventListener(object : GeoQueryEventListener{
            override fun onGeoQueryReady() {
            }
            override fun onKeyEntered(key: String?, location: GeoLocation?) {
                val locationQuery = database.getReference("places").child(key)
                locationQuery.addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot?) {
                        val place = p0?.getValue(Place::class.java)
                        if (place != null) {
                            //Get place image and add to Place object
                            val picStorage = FirebaseStorage.getInstance().reference.child("placepics").child(key + ".jpg").downloadUrl
                                    .addOnSuccessListener { uri ->
                                        place.img = uri.toString()
                                        callback.placeRetrieved(place)
                                    }
                                    .addOnFailureListener({ callback.onError() })
                        }
                    }

                    override fun onCancelled(p0: DatabaseError?) {
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