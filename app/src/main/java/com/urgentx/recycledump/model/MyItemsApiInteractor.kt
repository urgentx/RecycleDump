package com.urgentx.recycledump.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.urgentx.recycledump.model.callbacks.ItemDeletedCallback
import com.urgentx.recycledump.model.callbacks.MyItemsCallback
import com.urgentx.recycledump.util.Item


class MyItemsApiInteractor {

    private val database = FirebaseDatabase.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    fun getItems(callback: MyItemsCallback) {
        val usersReference = database.getReference("users")

        val uid = user!!.uid

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val itemNames = ArrayList<String>()
                for (itemSnapshot in dataSnapshot.children) {
                    var itemKey = itemSnapshot.key
                    Log.d("Firebase Database", "Item name is: $itemKey")
                    itemNames.add(itemKey)
                }
                retrieveItems(itemNames, callback)
            }

            override fun onCancelled(databaseError: DatabaseError) {


            }
        }
        usersReference.child(uid).child("items").addListenerForSingleValueEvent(postListener)
    }

    private fun retrieveItems(itemKeys: ArrayList<String>, callback: MyItemsCallback) {
        val itemsReference = database.getReference("items")

        val items = ArrayList<Item>()

        var i = 0 //Keep track of # items

        for (itemName in itemKeys) {
            val itemListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var item = dataSnapshot.getValue(Item::class.java)
                    item?.id = dataSnapshot.key
                    if (item != null) {
                        //Get place image and add to Place object
                        FirebaseStorage.getInstance().reference.child("itempics").child(dataSnapshot.key + ".jpg").downloadUrl
                                .addOnSuccessListener { uri ->
                                    item.img = uri.toString()
                                    items.add(item)
                                    if (i >= itemKeys.size - 1) {
                                        callback.itemsRetrieved(items)
                                    } else {
                                        i++
                                    }
                                }
                                .addOnFailureListener({
                                    item.img = ""
                                    items.add(item)
                                    if (i >= itemKeys.size - 1) {
                                        callback.itemsRetrieved(items)
                                    } else {
                                        i++
                                    }
                                })
                    } else {
                        i++
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {


                }
            }
            itemsReference.child(itemName).addListenerForSingleValueEvent(itemListener)
        }
    }

    fun deleteItem (itemID: String, callback: ItemDeletedCallback){
        val operation = database.reference.child("items").child(itemID).removeValue()
        operation.addOnSuccessListener { callback.itemDeleted(itemID) }
        operation.addOnFailureListener { callback.deleteError() }
    }
}