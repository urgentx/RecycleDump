package com.urgentx.recycledump.model.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.urgentx.recycledump.model.Settings
import com.urgentx.recycledump.util.firebase.FirebaseResult
import com.urgentx.recycledump.util.firebase.FirebaseResult.Companion.NETWORK_ERROR
import com.urgentx.recycledump.util.firebase.FirebaseResult.Companion.STRUCTURAL_ERROR
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SettingsApiInteractor {

    private val userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser!!.uid)

    fun updateSettings(settings: Settings): Observable<FirebaseResult> {
        val resultObservable = PublishSubject.create<FirebaseResult>()
        userRef.child("settings").setValue(settings, { databaseError, _ ->
            if (databaseError != null) {
                resultObservable.onNext(FirebaseResult.Success(Unit))
            } else {
                resultObservable.onNext(FirebaseResult.Error(STRUCTURAL_ERROR))
            }
        })
        return resultObservable
    }

    fun retrieveSettings(): Observable<FirebaseResult> {
        val resultObservable = PublishSubject.create<FirebaseResult>()
        userRef.child("settings").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(settingsSnapshot: DataSnapshot) {
                settingsSnapshot?.let {
                    val settings = it.getValue(Settings::class.java)
                    if (settings != null) {
                        resultObservable.onNext(FirebaseResult.Success(settings))
                    } else {
                        resultObservable.onNext(FirebaseResult.Error(STRUCTURAL_ERROR))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                resultObservable.onNext(FirebaseResult.Error(NETWORK_ERROR))
            }
        })
        return resultObservable
    }

}