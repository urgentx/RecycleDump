package com.urgentx.recycledump.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.urgentx.recycledump.util.firebase.FirebaseResult
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SettingsApiInteractor {

    val userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser?.uid)

    fun updateEmailPush (enabled: Boolean): Observable<FirebaseResult> {
        val resultObservable = PublishSubject.create<FirebaseResult>()
        userRef.child("pushn_mobile").setValue(enabled,{ databaseError, _ ->
            if (databaseError != null) {
                resultObservable.onNext(FirebaseResult.SUCCESS)
            } else {
                resultObservable.onNext(FirebaseResult.ERROR)
            }
        })
        return resultObservable
    }
}