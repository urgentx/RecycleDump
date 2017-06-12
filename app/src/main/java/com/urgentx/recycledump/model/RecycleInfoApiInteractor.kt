package com.urgentx.recycledump.model

import com.google.firebase.database.FirebaseDatabase
import com.urgentx.recycledump.model.callbacks.RecycleInfoCallback
import com.urgentx.recycledump.util.Item

class RecycleInfoApiInteractor {

    private val database = FirebaseDatabase.getInstance()
    val reference = database.getReference("items").push()

    fun storeItem(callback: RecycleInfoCallback, item: Item) {
        reference.setValue(item).addOnCompleteListener {

            callback.onSuccess() }
                .addOnFailureListener({

                    callback.onError() })
    }
}