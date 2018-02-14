package com.urgentx.recycledump.model.api

import com.google.firebase.database.FirebaseDatabase
import com.urgentx.recycledump.model.Collector
import com.urgentx.recycledump.util.firebase.FirebaseResult
import com.urgentx.recycledump.util.firebase.FirebaseResult.Companion.STRUCTURAL_ERROR
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by barcozaleta on 7/12/17.
 */
class CreateCollectorApiInteractor {

    private val placesReference = FirebaseDatabase.getInstance().reference.child("places")

    fun createCollector(collector: Collector): Observable<FirebaseResult> {

        val resultObservable = PublishSubject.create<FirebaseResult>()

        val newPlaceRef = placesReference.push()

        newPlaceRef.updateChildren(collector.toMap(), { error, _ ->
            error?.let {
                resultObservable.onNext(FirebaseResult.Error(STRUCTURAL_ERROR))
            } ?: resultObservable.onNext(FirebaseResult.Success(Unit))
        })

        return resultObservable
    }
}