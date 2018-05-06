package com.urgentx.recycledump.model.api

import android.net.Uri
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.urgentx.recycledump.model.Collector
import com.urgentx.recycledump.util.firebase.FirebaseResult
import com.urgentx.recycledump.util.firebase.FirebaseResult.Companion.NETWORK_ERROR
import com.urgentx.recycledump.util.firebase.FirebaseResult.Companion.STRUCTURAL_ERROR
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.io.File

/**
 * Created by barcozaleta on 7/12/17.
 */
class CreateCollectorApiInteractor {

    val placeLocationsRef = FirebaseDatabase.getInstance().getReference("placelocations") //Geofire directory
    private val placesReference = FirebaseDatabase.getInstance().reference.child("places")
    val geoFire = GeoFire(placeLocationsRef)

    fun createCollector(collector: Collector): Observable<FirebaseResult> {

        val resultObservable = PublishSubject.create<FirebaseResult>()

        val newPlaceRef = placesReference.push()

        geoFire.setLocation(newPlaceRef.key, GeoLocation(collector.lat, collector.long),
                { key, error ->
                    if (error != null) {
                        resultObservable.onNext(FirebaseResult.Error(STRUCTURAL_ERROR))
                    } else {
                        newPlaceRef.setValue(collector, { error, _ ->
                            error?.let {
                                resultObservable.onNext(FirebaseResult.Error(STRUCTURAL_ERROR))
                            } ?: resultObservable.onNext(FirebaseResult.Success(Unit))
                        })
                    }
                })

        //If user added pic, upload with place key to Firebase storage.

        val picUrl = collector.photoPath
        if (picUrl != null) {
            val picStorage = FirebaseStorage.getInstance().reference.child("placepics").child(newPlaceRef.key + ".jpg")
            val file = Uri.fromFile(File(picUrl))
            picStorage.putFile(file).addOnFailureListener { resultObservable.onNext(FirebaseResult.Error(NETWORK_ERROR)) }
        }

        return resultObservable
    }
}