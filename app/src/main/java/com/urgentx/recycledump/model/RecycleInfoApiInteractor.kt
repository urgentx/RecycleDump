package com.urgentx.recycledump.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.urgentx.recycledump.model.callbacks.RecycleInfoCallback
import com.urgentx.recycledump.util.Item

class RecycleInfoApiInteractor {

    private val database = FirebaseDatabase.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser


    fun storeItem(callback: RecycleInfoCallback, item: Item) {
        val itemsReference = database.getReference("items")
        val usersReference = database.getReference("users")

        val uid = user!!.uid

        val users = ArrayList<String>()
        users.add(uid)
        item.users = users

        itemsReference.child(item.name).setValue(item).addOnCompleteListener {
            callback.onSuccess()
        }
                .addOnFailureListener({
                    callback.onError()
                })

        val items = ArrayList<String>()
        items.add(item.name)

        usersReference.child(uid).child("items").child(item.name).setValue(items)


    }
}