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

        //Retrieve nearest places to user within set radius, then query the actual places node for the details of each one.
        geoFire.queryAtLocation(GeoLocation(latitude, longitude), 100.0).addGeoQueryEventListener(object : GeoQueryEventListener {
            override fun onGeoQueryReady() {
            }
            override fun onKeyEntered(key: String?, location: GeoLocation?) {
                val locationQuery = database.getReference("places").child(key)
                locationQuery.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot?) {
                        processGeoQueryResult(p0, key, callback)
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

    fun processGeoQueryResult(data: DataSnapshot?, key: String?, callback: PlaceCallback){
        val place = data?.getValue(Place::class.java)
        if (place != null) {
            //Get place image and add to Place object
            FirebaseStorage.getInstance().reference.child("placepics").child(key + ".jpg").downloadUrl
                    .addOnSuccessListener { uri ->
                        place.img = uri.toString()
                        callback.placeRetrieved(place)
                    }
                    .addOnFailureListener({
                        place.img = ""
                        callback.placeRetrieved(place)
                    })
        }
    }
}