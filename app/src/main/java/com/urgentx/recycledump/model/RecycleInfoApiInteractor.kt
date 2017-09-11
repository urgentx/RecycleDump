package com.urgentx.recycledump.model

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

        val key = itemsReference.push().key

        val uid = user!!.uid

        val users = ArrayList<String>()
        users.add(uid)
        item.users = users

        itemsReference.child(key).setValue(item).addOnFailureListener({
                    callback.onError()
                })

        usersReference.child(uid).child("items").child(key).setValue(true)

        if(imgPath != null) {
            val picStorage = FirebaseStorage.getInstance().reference.child("itempics").child(key + ".jpg")
            val file = Uri.fromFile(File(imgPath))
            val uploadTask = picStorage.putFile(file)

            uploadTask.addOnSuccessListener { callback.onSuccess() }
                    .addOnFailureListener { callback.onError() }
        }


    }
}