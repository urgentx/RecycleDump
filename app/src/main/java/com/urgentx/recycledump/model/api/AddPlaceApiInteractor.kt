package com.urgentx.recycledump.model.api

import android.net.Uri
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.urgentx.recycledump.model.callbacks.BinaryCallback
import com.urgentx.recycledump.model.callbacks.PlaceCallback
import com.urgentx.recycledump.util.Place
import com.google.firebase.storage.StorageReference
import java.io.File


class AddPlaceApiInteractor {

    private val database = FirebaseDatabase.getInstance()

    fun savePlace(place: Place, picUrl: String?,  callback: BinaryCallback) {
        val placeLocationsRef = database.getReference("placelocations") //Geofire directory
        val placesRef = database.getReference("places") //Places directory
        val geoFire = GeoFire(placeLocationsRef)

        //We save the place in places and its location data in placelocations.

        val key = placesRef.push().key

        geoFire.setLocation(key, GeoLocation(place.lat, place.long),
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

        //If user added pic, upload with place key to Firebase storage.

        if(picUrl != null) {
            val picStorage = FirebaseStorage.getInstance().reference.child("placepics").child(key + ".jpg")
            val file = Uri.fromFile(File(picUrl))
            val uploadTask = picStorage.putFile(file)

            uploadTask.addOnSuccessListener { callback.onBinarySuccess() }
                    .addOnFailureListener { callback.onBinaryError() }
        }
    }
}