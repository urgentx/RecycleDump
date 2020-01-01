package com.urgentx.recycledump.model.api

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.urgentx.recycledump.model.callbacks.RecycleInfoCallback
import com.urgentx.recycledump.util.Item
import java.io.File

class RecycleInfoApiInteractor {

    private val database = FirebaseDatabase.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser


    fun storeItem(callback: RecycleInfoCallback, item: Item, imgPath: String?) {
        val itemsReference = database.getReference("items")
        val usersReference = database.getReference("users")

        val key = itemsReference.push().key!!

        val uid = user!!.uid

        itemsReference.child(key).setValue(item).addOnFailureListener({
            callback.onError()
        })

        itemsReference.child(key).child("users").child(uid).setValue(true)

        usersReference.child(uid).child("items").child(key).setValue(item.category)

        if (imgPath != null) {
            val picStorage = FirebaseStorage.getInstance().reference.child("itempics").child(key + ".jpg")
            val file = Uri.fromFile(File(imgPath))
            val uploadTask = picStorage.putFile(file)

            uploadTask.addOnSuccessListener { callback.onSuccess() }
                    .addOnFailureListener { callback.onError() }
        } else {
            callback.onSuccess()
        }
    }
}